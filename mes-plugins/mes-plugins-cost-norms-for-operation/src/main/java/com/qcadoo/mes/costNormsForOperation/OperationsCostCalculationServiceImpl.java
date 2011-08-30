package com.qcadoo.mes.costNormsForOperation;

import static com.google.common.base.Preconditions.checkArgument;
import static com.qcadoo.mes.orders.constants.OrdersConstants.MODEL_ORDER;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qcadoo.mes.costNormsForOperation.constants.OperationsCostCalculationConstants;
import com.qcadoo.mes.productionScheduling.OrderRealizationTimeService;
import com.qcadoo.model.api.DataDefinition;
import com.qcadoo.model.api.Entity;
import com.qcadoo.model.api.EntityList;
import com.qcadoo.model.api.EntityTree;
import com.qcadoo.model.api.EntityTreeNode;

@Service
public class OperationsCostCalculationServiceImpl implements OperationsCostCalculationService {

    @Autowired
    private OrderRealizationTimeService orderRealizationTimeService;

    private static final String OPERATION_NODE_ENTITY_TYPE = "operation";

    @Override
    public Map<String, BigDecimal> calculateOperationsCost(Entity source,
            OperationsCostCalculationConstants calculateOperationCostsMode, boolean includeTPZ, BigDecimal quantity) {
        checkArgument(quantity != null, "quantity is null");
        checkArgument(quantity.compareTo(BigDecimal.valueOf(0)) == 1, "quantity should be greather than 0");
        checkArgument(source != null, "source entity is null");
        EntityTree operationComponents;
        BigDecimal totalMachineHourlyCost = new BigDecimal(0);
        BigDecimal totalLaborHourlyCost = new BigDecimal(0);
        BigDecimal totalPieceWorkCost = new BigDecimal(0);
        Map<String, BigDecimal> result = new HashMap<String, BigDecimal>();

        DataDefinition dataDefinition = source.getDataDefinition();

        if (MODEL_ORDER.equals(dataDefinition.getName())) {
            operationComponents = source.getTreeField("orderOperationComponents");
        } else {
            operationComponents = source.getTreeField("operationComponents");
        }
        if (operationComponents == null) {
            throw new IllegalArgumentException("Incompatible source entity type..");
        }

        if (calculateOperationCostsMode == OperationsCostCalculationConstants.PIECEWORK) {
            totalPieceWorkCost = estimateCostCalculationForPieceWork(operationComponents.getRoot(), quantity, includeTPZ);
        }
        if (calculateOperationCostsMode == OperationsCostCalculationConstants.HOURLY) {

            int time = orderRealizationTimeService.estimateRealizationTimeForOperation(operationComponents.getRoot(), quantity,
                    includeTPZ);

            if (time == 0) {
                totalLaborHourlyCost = new BigDecimal(0);
                totalMachineHourlyCost = new BigDecimal(0);
            } else {
                totalLaborHourlyCost = estimateCostCalculationForHourly(operationComponents.getRoot(), quantity, includeTPZ,
                        OperationsCostCalculationConstants.LABOR_HOURLY_COST);
                totalMachineHourlyCost = estimateCostCalculationForHourly(operationComponents.getRoot(), quantity, includeTPZ,
                        OperationsCostCalculationConstants.MACHINE_HOURLY_COST);
            }
        }
        result.put("totalMachineHourlyCosts", totalMachineHourlyCost);
        result.put("totalLaborHourlyCosts", totalLaborHourlyCost);
        result.put("totalPieceworkCosts", totalPieceWorkCost);
        return result;
    }

    public BigDecimal estimateCostCalculationForHourly(final EntityTreeNode operationComponent, final BigDecimal plannedQuantity,
            Boolean includeTPZ, String hourly) {
        if (operationComponent.getField("entityType") != null
                && !OPERATION_NODE_ENTITY_TYPE.equals(operationComponent.getField("entityType"))) {
            return estimateCostCalculationForHourly(
                    operationComponent.getBelongsToField("referenceTechnology").getTreeField("operationComponents").getRoot(),
                    plannedQuantity, includeTPZ, hourly);
        } else {
            BigDecimal pathCost = new BigDecimal(0);
            for (EntityTreeNode child : operationComponent.getChildren()) {
                BigDecimal tmpPathCost = estimateCostCalculationForHourly(child, plannedQuantity, includeTPZ, hourly);
                if (tmpPathCost.compareTo(pathCost) == 1) {
                    pathCost = tmpPathCost;
                }
            }
            Double time = new Double(orderRealizationTimeService.estimateRealizationTimeForOperation(operationComponent,
                    plannedQuantity, includeTPZ));
            BigDecimal realizationTime = BigDecimal.valueOf(time / 3600);
            BigDecimal hourlyCost = (BigDecimal) operationComponent.getField(hourly);
            if (hourlyCost == null) {
                hourlyCost = getHourlyCost(operationComponent, hourlyCost, hourly);
                // hourlyCost = new BigDecimal(0);
            }
            BigDecimal operationCost = realizationTime.multiply(hourlyCost).setScale(8, BigDecimal.ROUND_UP);
            pathCost = pathCost.add(operationCost);

            return pathCost;
        }
    }

    private BigDecimal getHourlyCost(final EntityTreeNode operationComponent, BigDecimal hourlyCost, String hourly) {

        hourlyCost = (BigDecimal) operationComponent.getBelongsToField("technology").getField(hourly);
        if (hourlyCost == null) {
            hourlyCost = (BigDecimal) operationComponent.getBelongsToField("operation").getField(hourly);
        } else {
            hourlyCost = new BigDecimal(0);
        }

        return hourlyCost;
    }

    private BigDecimal estimateCostCalculationForPieceWork(final EntityTreeNode operationComponent,
            final BigDecimal plannedQuantity, Boolean includeTPZ) {

        if (operationComponent.getField("entityType") != null
                && !OPERATION_NODE_ENTITY_TYPE.equals(operationComponent.getField("entityType"))) {
            return estimateCostCalculationForPieceWork(
                    operationComponent.getBelongsToField("referenceTechnology").getTreeField("operationComponents").getRoot(),
                    plannedQuantity, includeTPZ);
        } else {
            BigDecimal operationCost = new BigDecimal(0);
            BigDecimal pathCost = new BigDecimal(0);
            for (EntityTreeNode child : operationComponent.getChildren()) {
                BigDecimal tmpPathCost = estimateCostCalculationForPieceWork(child, plannedQuantity, includeTPZ);
                if (tmpPathCost.compareTo(pathCost) == 1) {
                    pathCost = tmpPathCost;
                }
            }
            BigDecimal piecework = (BigDecimal) operationComponent.getField("pieceworkCost");
            if (piecework == null) {
                piecework = new BigDecimal(0);
            }
            BigDecimal numberOfOperations = new BigDecimal(operationComponent.getField("numberOfOperations").toString());
            if (numberOfOperations.equals(null)) {
                numberOfOperations = new BigDecimal(1);
            }
            BigDecimal pieceWorkCost = piecework.divide(numberOfOperations);
            BigDecimal totalQuantityOutputProduct = new BigDecimal(0);
            EntityList outputProducts = operationComponent.getHasManyField("operationProductOutComponents");

            if (!outputProducts.isEmpty()) {
                for (Entity outputProduct : outputProducts) {
                    totalQuantityOutputProduct = totalQuantityOutputProduct.add((BigDecimal) outputProduct.getField("quantity"));
                }
            }
            operationCost = operationCost.add(pieceWorkCost.multiply(totalQuantityOutputProduct))
                    .setScale(4, BigDecimal.ROUND_UP);

            pathCost = pathCost.add(operationCost);
            return pathCost;
        }
    }

}
