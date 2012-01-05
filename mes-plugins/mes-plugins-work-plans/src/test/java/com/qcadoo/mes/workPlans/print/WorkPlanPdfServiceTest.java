package com.qcadoo.mes.workPlans.print;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.qcadoo.localization.api.TranslationService;
import com.qcadoo.localization.api.utils.DateUtils;
import com.qcadoo.mes.workPlans.constants.WorkPlanType;
import com.qcadoo.model.api.Entity;
import com.qcadoo.model.api.EntityTree;
import com.qcadoo.report.api.PrioritizedString;
import com.qcadoo.report.api.pdf.PdfUtil;
import com.qcadoo.security.api.SecurityService;

public class WorkPlanPdfServiceTest {

    private static final String NO_WORKSTATION_TYPE = "No workstation type";

    private static final String NO_DIVISION = "No division";

    private static final String BY_DIVISION = "By division";

    private static final String BY_WORKSTATION_TYPE = "By workstation type";

    private static final String BY_END_PRODUCT = "By end product";

    private static final String NO_DISTINCTION = "No distinction";

    private WorkPlanPdfService2 workPlanPdfService;

    private TranslationService translationService;

    private SecurityService securityService;

    private Locale locale;

    private Document document;

    private Entity op1Comp, op2Comp, op3Comp, op4Comp, op5Comp;

    private Entity workstation1, workstation2;

    private Entity workPlan;

    private Map<Entity, Entity> operationComponent2order = new HashMap<Entity, Entity>();

    @Before
    public void init() {
        workPlanPdfService = new WorkPlanPdfService2();

        translationService = mock(TranslationService.class);
        securityService = mock(SecurityService.class);

        ReflectionTestUtils.setField(workPlanPdfService, "translationService", translationService);
        ReflectionTestUtils.setField(workPlanPdfService, "securityService", securityService);

        locale = Locale.getDefault();
        document = mock(Document.class);
        workPlan = mock(Entity.class);

        op1Comp = mock(Entity.class);
        op2Comp = mock(Entity.class);
        op3Comp = mock(Entity.class);
        op4Comp = mock(Entity.class);
        op5Comp = mock(Entity.class);

        workstation1 = mock(Entity.class);
        workstation2 = mock(Entity.class);

        Entity division1 = mock(Entity.class);
        Entity division2 = mock(Entity.class);

        when(division1.getStringField("name")).thenReturn("division1");
        when(division2.getStringField("name")).thenReturn("division2");

        when(workstation1.getBelongsToField("division")).thenReturn(division1);
        when(workstation2.getBelongsToField("division")).thenReturn(division2);

        when(workstation1.getStringField("name")).thenReturn("workstation1");
        when(workstation2.getStringField("name")).thenReturn("workstation2");

        Entity op1 = mock(Entity.class);
        Entity op2 = mock(Entity.class);
        Entity op3 = mock(Entity.class);
        Entity op4 = mock(Entity.class);
        Entity op5 = mock(Entity.class);

        when(op1Comp.getBelongsToField("operation")).thenReturn(op1);
        when(op2Comp.getBelongsToField("operation")).thenReturn(op2);
        when(op3Comp.getBelongsToField("operation")).thenReturn(op3);
        when(op4Comp.getBelongsToField("operation")).thenReturn(op4);
        when(op5Comp.getBelongsToField("operation")).thenReturn(op5);

        when(op1.getBelongsToField("workstationType")).thenReturn(workstation1);
        when(op2.getBelongsToField("workstationType")).thenReturn(workstation1);
        when(op3.getBelongsToField("workstationType")).thenReturn(workstation2);
        when(op4.getBelongsToField("workstationType")).thenReturn(workstation2);
        when(op5.getBelongsToField("workstationType")).thenReturn(null);

        @SuppressWarnings("unchecked")
        List<Entity> orders = mock(List.class);

        Entity order1 = mock(Entity.class);
        Entity order2 = mock(Entity.class);

        when(order1.getStringField("number")).thenReturn("1");
        when(order2.getStringField("number")).thenReturn("2");

        @SuppressWarnings("unchecked")
        Iterator<Entity> ordersIterator = mock(Iterator.class);
        when(orders.iterator()).thenReturn(ordersIterator);
        when(ordersIterator.hasNext()).thenReturn(true, true, false);
        when(ordersIterator.next()).thenReturn(order1, order2);

        Entity tech1 = mock(Entity.class);
        Entity tech2 = mock(Entity.class);

        when(order1.getBelongsToField("technology")).thenReturn(tech1);
        when(order2.getBelongsToField("technology")).thenReturn(tech2);

        Entity prod1 = mock(Entity.class);
        Entity prod2 = mock(Entity.class);

        when(tech1.getBelongsToField("product")).thenReturn(prod1);
        when(tech2.getBelongsToField("product")).thenReturn(prod2);

        when(prod1.getStringField("name")).thenReturn("product1");
        when(prod2.getStringField("name")).thenReturn("product2");

        EntityTree operComp1 = mock(EntityTree.class);
        EntityTree operComp2 = mock(EntityTree.class);

        when(tech1.getTreeField("operationComponents")).thenReturn(operComp1);
        when(tech2.getTreeField("operationComponents")).thenReturn(operComp2);

        @SuppressWarnings("unchecked")
        Iterator<Entity> operComp1Iterator = mock(Iterator.class);
        when(operComp1.iterator()).thenReturn(operComp1Iterator);
        when(operComp1Iterator.hasNext()).thenReturn(true, true, true, false);
        when(operComp1Iterator.next()).thenReturn(op1Comp, op2Comp, op3Comp);

        @SuppressWarnings("unchecked")
        Iterator<Entity> operComp2Iterator = mock(Iterator.class);
        when(operComp2.iterator()).thenReturn(operComp2Iterator);
        when(operComp2Iterator.hasNext()).thenReturn(true, true, false);
        when(operComp2Iterator.next()).thenReturn(op4Comp, op5Comp);

        when(op1Comp.getStringField("nodeNumber")).thenReturn("2.A.1");
        when(op2Comp.getStringField("nodeNumber")).thenReturn("1.");
        when(op3Comp.getStringField("nodeNumber")).thenReturn("2.");
        when(op4Comp.getStringField("nodeNumber")).thenReturn("1.");
        when(op5Comp.getStringField("nodeNumber")).thenReturn("2.");

        when(workPlan.getManyToManyField("orders")).thenReturn(orders);

        when(translationService.translate("workPlans.workPlan.report.title.noDistinction", locale)).thenReturn(NO_DISTINCTION);
        when(translationService.translate("workPlans.workPlan.report.title.byWorkstationType", locale)).thenReturn(
                BY_WORKSTATION_TYPE);
        when(translationService.translate("workPlans.workPlan.report.title.noWorkstationType", locale)).thenReturn(
                NO_WORKSTATION_TYPE);
        when(translationService.translate("workPlans.workPlan.report.title.byEndProduct", locale)).thenReturn(BY_END_PRODUCT);
        when(translationService.translate("workPlans.workPlan.report.title.byDivision", locale)).thenReturn(BY_DIVISION);
        when(translationService.translate("workPlans.workPlan.report.title.noDivision", locale)).thenReturn(NO_DIVISION);
    }

    @Test
    public void shouldHasCorrectReportTitle() {
        // given
        String correctTitle = "title";
        when(translationService.translate("workPlans.workPlan.report.title", locale)).thenReturn(correctTitle);

        // when
        String title = workPlanPdfService.getReportTitle(locale);

        // then
        assertEquals(correctTitle, title);
    }

    @Test
    public void shouldAddCorrectMainHeader() throws DocumentException {
        // given
        Date date = mock(Date.class);
        Object name = mock(Object.class);
        when(name.toString()).thenReturn("name");
        when(workPlan.getField("date")).thenReturn(date);
        when(workPlan.getField("name")).thenReturn(name);
        when(securityService.getCurrentUserName()).thenReturn("userName");

        // when
        workPlanPdfService.addMainHeader(document, workPlan, locale);

        // then
        verify(translationService).translate("workPlans.workPlan.report.title", locale);
        verify(translationService).translate("qcadooReport.commons.generatedBy.label", locale);
    }

    @Test
    public void shouldPrepareCorrectOrdersTableHeader() throws DocumentException {
        // given
        when(translationService.translate("workPlans.workPlan.report.paragrah", locale)).thenReturn("paragraph");
        when(translationService.translate("orders.order.number.label", locale)).thenReturn("numberLabel");
        when(translationService.translate("orders.order.name.label", locale)).thenReturn("nameLabel");
        when(translationService.translate("workPlans.workPlan.report.colums.product", locale)).thenReturn("productLabel");
        when(translationService.translate("orders.order.plannedQuantity.label", locale)).thenReturn("quantityLabel");
        when(translationService.translate("orders.order.dateTo.label", locale)).thenReturn("dateLabel");

        // when
        List<String> ordersTableHeader = workPlanPdfService.prepareOrdersTableHeader(document, workPlan, locale);

        // then
        Iterator<String> iterator = ordersTableHeader.iterator();
        assertEquals("numberLabel", iterator.next());
        assertEquals("nameLabel", iterator.next());
        assertEquals("productLabel", iterator.next());
        assertEquals("quantityLabel", iterator.next());
        assertEquals("dateLabel", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void shouldAddOrdersToTheTableCorrectly() throws DocumentException {
        // given
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtils.DATE_FORMAT);

        DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(Locale.getDefault());

        PdfPTable table = mock(PdfPTable.class);
        PdfPCell defaultCell = mock(PdfPCell.class);
        Entity order = mock(Entity.class);
        Entity product = mock(Entity.class);
        Date now = new Date();
        BigDecimal quantity = new BigDecimal(10);
        List<Entity> orders = new ArrayList<Entity>();
        orders.add(order);

        when(table.getDefaultCell()).thenReturn(defaultCell);

        when(order.getField("number")).thenReturn("orderNumber");
        when(order.getField("name")).thenReturn("orderName");
        when(order.getField("plannedQuantity")).thenReturn(quantity);
        when(order.getField("product")).thenReturn(product);

        when(product.getField("number")).thenReturn("productNumber");
        when(product.getField("name")).thenReturn("productName");
        when(product.getField("unit")).thenReturn("productUnit");

        when(order.getField("dateTo")).thenReturn(now);

        // when
        workPlanPdfService.addOrderSeries(table, orders, df);

        // then
        ArgumentCaptor<Phrase> phrase = ArgumentCaptor.forClass(Phrase.class);
        verify(table, times(5)).addCell(phrase.capture());

        assertEquals("orderNumber", phrase.getAllValues().get(0).getContent());
        assertEquals("orderName", phrase.getAllValues().get(1).getContent());
        assertEquals("productName (productNumber)", phrase.getAllValues().get(2).getContent());
        assertEquals(df.format(quantity) + " productUnit", phrase.getAllValues().get(3).getContent());
        assertEquals(simpleDateFormat.format(now), phrase.getAllValues().get(4).getContent());

        assertEquals(PdfUtil.getArialRegular9Dark(), phrase.getAllValues().get(0).getFont());
        assertEquals(PdfUtil.getArialRegular9Dark(), phrase.getAllValues().get(1).getFont());
        assertEquals(PdfUtil.getArialRegular9Dark(), phrase.getAllValues().get(2).getFont());
        assertEquals(PdfUtil.getArialRegular9Dark(), phrase.getAllValues().get(3).getFont());
        assertEquals(PdfUtil.getArialRegular9Dark(), phrase.getAllValues().get(4).getFont());
    }

    @Test
    public void shouldNotTryToAddProductToTheOrdersTableIfThereIsNone() throws DocumentException {
        // given
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtils.DATE_FORMAT);

        DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(Locale.getDefault());

        PdfPTable table = mock(PdfPTable.class);
        PdfPCell defaultCell = mock(PdfPCell.class);
        Entity order = mock(Entity.class);
        Date now = new Date();
        BigDecimal quantity = new BigDecimal(10);
        List<Entity> orders = new ArrayList<Entity>();
        orders.add(order);

        when(table.getDefaultCell()).thenReturn(defaultCell);

        when(order.getField("number")).thenReturn("orderNumber");
        when(order.getField("name")).thenReturn("orderName");
        when(order.getField("plannedQuantity")).thenReturn(quantity);

        when(order.getField("dateTo")).thenReturn(now);

        // when
        workPlanPdfService.addOrderSeries(table, orders, df);

        // then
        ArgumentCaptor<Phrase> phrase = ArgumentCaptor.forClass(Phrase.class);
        verify(table, times(5)).addCell(phrase.capture());

        assertEquals("orderNumber", phrase.getAllValues().get(0).getContent());
        assertEquals("orderName", phrase.getAllValues().get(1).getContent());
        assertEquals("", phrase.getAllValues().get(2).getContent());
        assertEquals(df.format(quantity), phrase.getAllValues().get(3).getContent());
        assertEquals(simpleDateFormat.format(now), phrase.getAllValues().get(4).getContent());

        assertEquals(PdfUtil.getArialRegular9Dark(), phrase.getAllValues().get(0).getFont());
        assertEquals(PdfUtil.getArialRegular9Dark(), phrase.getAllValues().get(1).getFont());
        assertEquals(PdfUtil.getArialRegular9Dark(), phrase.getAllValues().get(2).getFont());
        assertEquals(PdfUtil.getArialRegular9Dark(), phrase.getAllValues().get(3).getFont());
        assertEquals(PdfUtil.getArialRegular9Dark(), phrase.getAllValues().get(4).getFont());
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionForIncorectWorkPlanType() {
        // given
        when(workPlan.getStringField("type")).thenReturn("asdfghjk");

        // when
        workPlanPdfService.getOperationsTitle(workPlan);
    }

    @Test
    public void shouldGetOperationComponentsForNoDistinction() {
        // given
        when(workPlan.getStringField("type")).thenReturn(WorkPlanType.NO_DISTINCTION.getStringValue());

        // when
        Map<PrioritizedString, List<Entity>> operationComponents = workPlanPdfService.getOperationComponentsWithDistinction(
                workPlan, operationComponent2order, locale);

        // then
        PrioritizedString noDistinction = new PrioritizedString(NO_DISTINCTION);
        assertTrue(operationComponents.get(noDistinction).get(0).equals(op2Comp));
        assertTrue(operationComponents.get(noDistinction).get(1).equals(op3Comp));
        assertTrue(operationComponents.get(noDistinction).get(2).equals(op1Comp));
        assertTrue(operationComponents.get(noDistinction).get(3).equals(op4Comp));
        assertTrue(operationComponents.get(noDistinction).get(4).equals(op5Comp));
    }

    @Test
    public void shouldGetOperationComponentsForDistinctionByWorkstationType() {
        // given
        when(workPlan.getStringField("type")).thenReturn(WorkPlanType.BY_WORKSTATION_TYPE.getStringValue());

        // when
        Map<PrioritizedString, List<Entity>> operationComponents = workPlanPdfService.getOperationComponentsWithDistinction(
                workPlan, operationComponent2order, locale);

        // then
        PrioritizedString workstation1String = new PrioritizedString(BY_WORKSTATION_TYPE + " {workstation1}");
        PrioritizedString workstation2String = new PrioritizedString(BY_WORKSTATION_TYPE + " {workstation2}");
        PrioritizedString noWorkstationString = new PrioritizedString(NO_WORKSTATION_TYPE, 1);

        assertTrue(operationComponents.get(workstation1String).get(0).equals(op2Comp));
        assertTrue(operationComponents.get(workstation1String).get(1).equals(op1Comp));
        assertTrue(operationComponents.get(workstation2String).get(0).equals(op3Comp));
        assertTrue(operationComponents.get(workstation2String).get(1).equals(op4Comp));
        assertTrue(operationComponents.get(noWorkstationString).get(0).equals(op5Comp));
    }

    @Test
    public void shouldGetOperationComponentsForDistinctionByDivision() {
        // given
        when(workPlan.getStringField("type")).thenReturn(WorkPlanType.BY_DIVISION.getStringValue());

        // when
        Map<PrioritizedString, List<Entity>> operationComponents = workPlanPdfService.getOperationComponentsWithDistinction(
                workPlan, operationComponent2order, locale);

        // then
        PrioritizedString division1String = new PrioritizedString(BY_DIVISION + " {division1}");
        PrioritizedString division2String = new PrioritizedString(BY_DIVISION + " {division2}");
        PrioritizedString noDivisionString = new PrioritizedString(NO_DIVISION, 1);

        assertTrue(operationComponents.get(division1String).get(0).equals(op2Comp));
        assertTrue(operationComponents.get(division1String).get(1).equals(op1Comp));
        assertTrue(operationComponents.get(division2String).get(0).equals(op3Comp));
        assertTrue(operationComponents.get(division2String).get(1).equals(op4Comp));
        assertTrue(operationComponents.get(noDivisionString).get(0).equals(op5Comp));
    }

    @Test
    public void shouldGetOperationComponentsForDistinctionByEndProduct() {
        // given
        when(workPlan.getStringField("type")).thenReturn(WorkPlanType.BY_END_PRODUCT.getStringValue());

        // when
        Map<PrioritizedString, List<Entity>> operationComponents = workPlanPdfService.getOperationComponentsWithDistinction(
                workPlan, operationComponent2order, locale);

        // then
        PrioritizedString product1String = new PrioritizedString(BY_END_PRODUCT + " {product1}");
        PrioritizedString product2String = new PrioritizedString(BY_END_PRODUCT + " {product2}");

        assertTrue(operationComponents.get(product1String).get(0).equals(op2Comp));
        assertTrue(operationComponents.get(product1String).get(1).equals(op3Comp));
        assertTrue(operationComponents.get(product1String).get(2).equals(op1Comp));
        assertTrue(operationComponents.get(product2String).get(0).equals(op4Comp));
        assertTrue(operationComponents.get(product2String).get(1).equals(op5Comp));
    }

    @Test
    public void shouldAddCorrectAmountOfOperationsInCaseOfNoDistinction() throws DocumentException {
        // given
        when(workPlan.getStringField("type")).thenReturn(WorkPlanType.NO_DISTINCTION.getStringValue());

        // when
        workPlanPdfService.addOperations(document, workPlan, locale);

        // then
        verify(document, times(1)).add(Chunk.NEXTPAGE);
        verify(document, times(7)).add(any(PdfPTable.class));
    }

    @Test
    public void shouldAddCorrectAmountOfOperationsInCaseOfDistinctionByWorkstationType() throws DocumentException {
        // given
        when(workPlan.getStringField("type")).thenReturn(WorkPlanType.BY_WORKSTATION_TYPE.getStringValue());

        // when
        workPlanPdfService.addOperations(document, workPlan, locale);

        // then
        verify(document, times(3)).add(Chunk.NEXTPAGE);
        verify(document, times(11)).add(any(PdfPTable.class));
    }
}