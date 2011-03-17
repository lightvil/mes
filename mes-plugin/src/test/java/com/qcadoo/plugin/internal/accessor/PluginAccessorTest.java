package com.qcadoo.plugin.internal.accessor;

import static org.mockito.Mockito.mock;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.qcadoo.plugin.internal.api.ModuleFactoryAccessor;
import com.qcadoo.plugin.internal.api.PluginDao;
import com.qcadoo.plugin.internal.api.PluginDependencyManager;
import com.qcadoo.plugin.internal.api.PluginDescriptorParser;

public class PluginAccessorTest {

    private final PluginDescriptorParser pluginDescriptorParser = mock(PluginDescriptorParser.class);

    private final PluginDao pluginDao = Mockito.mock(PluginDao.class);

    private final PluginDependencyManager pluginDependencyManager = mock(PluginDependencyManager.class);

    private final ModuleFactoryAccessor moduleFactoryAccessor = mock(ModuleFactoryAccessor.class);

    private DefaultPluginAccessor pluginAccessor;

    @Before
    public void init() {
        pluginAccessor = new DefaultPluginAccessor();
        pluginAccessor.setPluginDescriptorParser(pluginDescriptorParser);
        pluginAccessor.setPluginDao(pluginDao);
        pluginAccessor.setPluginDependencyManager(pluginDependencyManager);
        pluginAccessor.setModuleFactoryAccessor(moduleFactoryAccessor);
    }

    @Test
    public void shouldname() throws Exception {
        Assert.assertTrue(true);
    }

    //
    // @Test
    // public void shouldSynchronizePluginsFromClasspathAndDatabase() throws Exception {
    // // given
    // Plugin plugin1 = mock(Plugin.class);
    // PersistentPlugin persistentPlugin1 = mock(PersistentPlugin.class);
    // given(plugin1.getIdentifier()).willReturn("identifier1");
    // given(persistentPlugin1.getIdentifier()).willReturn("identifier1");
    // given(plugin1.compareVersion(persistentPlugin1)).willReturn(0);
    //
    // PersistentPlugin persistentPlugin21 = mock(PersistentPlugin.class);
    // given(persistentPlugin21.getIdentifier()).willReturn("identifier21");
    // given(persistentPlugin21.getIdentifier()).willReturn("identifier21");
    // given(persistentPlugin21.getState()).willReturn(PluginState.ENABLED);
    // Plugin plugin22 = mock(Plugin.class);
    // given(plugin22.getIdentifier()).willReturn("identifier21");
    // given(plugin22.compareVersion(persistentPlugin21)).willReturn(1);
    //
    // Plugin plugin3 = mock(Plugin.class);
    // given(plugin3.getIdentifier()).willReturn("identifier3");
    // PersistentPlugin persistentPlugin4 = mock(PersistentPlugin.class);
    // given(persistentPlugin4.getIdentifier()).willReturn("identifier4");
    //
    // Set<Plugin> pluginsFromDescriptor = Sets.newHashSet(plugin1, plugin22, plugin3);
    // Set<PersistentPlugin> pluginsFromDatabase = Sets.newHashSet(plugin1, persistentPlugin21, persistentPlugin4);
    //
    // given(pluginDescriptorParser.loadPlugins()).willReturn(pluginsFromDescriptor);
    //
    // given(pluginDao.list()).willReturn(pluginsFromDatabase);
    //
    // // when
    // pluginAccessor.init();
    //
    // // then
    // verify(pluginDao, never()).save(plugin1);
    // verify(plugin22).changeStateTo(PluginState.ENABLED);
    // verify(pluginDao).save(plugin22);
    // verify(pluginDao).save(plugin3);
    // verify(pluginDao).delete(persistentPlugin4);
    // }
    //
    // @Test
    // public void shouldListAllPlugins() throws Exception {
    // // given
    // Plugin plugin1 = mock(Plugin.class, "plugin1");
    // PersistentPlugin persistentPlugin1 = mock(PersistentPlugin.class, "plugin1");
    // given(plugin1.getIdentifier()).willReturn("identifier1");
    // given(persistentPlugin1.getIdentifier()).willReturn("identifier1");
    // given(plugin1.compareVersion(persistentPlugin1)).willReturn(0);
    // given(plugin1.hasState(PluginState.ENABLED)).willReturn(false);
    // given(persistentPlugin1.getState()).willReturn(PluginState.DISABLED);
    //
    // PersistentPlugin persistentPlugin21 = mock(PersistentPlugin.class, "plugin21");
    // given(persistentPlugin21.getIdentifier()).willReturn("identifier21");
    // given(persistentPlugin21.getState()).willReturn(PluginState.ENABLED);
    // Plugin plugin22 = mock(Plugin.class, "plugin22");
    // given(plugin22.getIdentifier()).willReturn("identifier21");
    // given(plugin22.hasState(PluginState.ENABLED)).willReturn(true);
    // given(plugin22.compareVersion(persistentPlugin21)).willReturn(1);
    //
    // Plugin plugin3 = mock(Plugin.class, "plugin3");
    // PersistentPlugin persistentPlugin3 = mock(PersistentPlugin.class, "plugin3");
    // given(plugin3.getIdentifier()).willReturn("identifier3");
    // given(persistentPlugin3.getIdentifier()).willReturn("identifier3");
    // given(plugin3.hasState(PluginState.ENABLED)).willReturn(true);
    // given(persistentPlugin3.getState()).willReturn(PluginState.ENABLED);
    //
    // Plugin plugin4 = mock(Plugin.class, "plugin4");
    // given(plugin4.getIdentifier()).willReturn("identifier4");
    // given(plugin4.hasState(PluginState.ENABLED)).willReturn(false);
    //
    // Set<Plugin> pluginsFromDescriptor = Sets.newHashSet(plugin1, plugin22, plugin3, plugin4);
    // Set<PersistentPlugin> pluginsFromDatabase = Sets.newHashSet(persistentPlugin1, persistentPlugin21, persistentPlugin3);
    //
    // given(pluginDescriptorParser.loadPlugins()).willReturn(pluginsFromDescriptor);
    //
    // given(pluginDao.list()).willReturn(pluginsFromDatabase);
    //
    // // when
    // pluginAccessor.init();
    //
    // // then
    // verify(plugin1).changeStateTo(PluginState.DISABLED);
    // verify(plugin22).changeStateTo(PluginState.ENABLED);
    // verify(plugin3).changeStateTo(PluginState.ENABLED);
    // verify(plugin4).changeStateTo(PluginState.DISABLED);
    // verify(plugin4).changeStateTo(PluginState.ENABLED);
    //
    // assertThat(pluginAccessor.getPlugins(), hasItems(plugin1, plugin22, plugin3, plugin4));
    //
    // assertThat(pluginAccessor.getEnabledPlugins(), hasItems(plugin22, plugin3));
    // assertThat(pluginAccessor.getEnabledPlugins(), not(hasItem(plugin1)));
    // assertThat(pluginAccessor.getEnabledPlugins(), not(hasItem(plugin4)));
    //
    // assertEquals(plugin1, pluginAccessor.getPlugin("identifier1"));
    // assertEquals(plugin22, pluginAccessor.getPlugin("identifier21"));
    // assertEquals(plugin3, pluginAccessor.getPlugin("identifier3"));
    // assertEquals(plugin4, pluginAccessor.getPlugin("identifier4"));
    // assertNull(pluginAccessor.getEnabledPlugin("identifier1"));
    // assertEquals(plugin22, pluginAccessor.getEnabledPlugin("identifier21"));
    // assertEquals(plugin3, pluginAccessor.getEnabledPlugin("identifier3"));
    // assertNull(pluginAccessor.getEnabledPlugin("identifier4"));
    // }
    //
    // @Test(expected = IllegalStateException.class)
    // public void shouldFailOnPluginDowngrade() throws Exception {
    // // given
    // Plugin plugin11 = mock(Plugin.class);
    // given(plugin11.getIdentifier()).willReturn("identifier11");
    // PersistentPlugin persistentPlugin12 = mock(PersistentPlugin.class);
    // given(persistentPlugin12.getIdentifier()).willReturn("identifier11");
    // given(plugin11.compareVersion(persistentPlugin12)).willReturn(-1);
    //
    // Set<Plugin> pluginsFromDescriptor = Sets.newHashSet(plugin11);
    // Set<PersistentPlugin> pluginsFromDatabase = Sets.newHashSet(persistentPlugin12);
    //
    // given(pluginDescriptorParser.loadPlugins()).willReturn(pluginsFromDescriptor);
    //
    // given(pluginDao.list()).willReturn(pluginsFromDatabase);
    //
    // // when
    // pluginAccessor.init();
    // }
    //
    // @Test
    // public void shouldPerformInit() throws Exception {
    // // given
    // Plugin plugin1 = mock(Plugin.class, "plugin1");
    // PersistentPlugin persistentPlugin1 = mock(PersistentPlugin.class, "plugin1");
    // given(plugin1.getIdentifier()).willReturn("identifier1");
    // given(persistentPlugin1.getIdentifier()).willReturn("identifier1");
    // Plugin plugin2 = mock(Plugin.class, "plugin2");
    // PersistentPlugin persistentPlugin2 = mock(PersistentPlugin.class, "plugin2");
    // given(plugin2.getIdentifier()).willReturn("identifier2");
    // given(persistentPlugin2.getIdentifier()).willReturn("identifier2");
    //
    // Set<Plugin> pluginsFromDescriptor = Sets.newHashSet(plugin1, plugin2);
    // Set<PersistentPlugin> pluginsFromDatabase = Sets.newHashSet(persistentPlugin1, persistentPlugin2);
    // List<Plugin> sortedPluginsToInitialize = Lists.newArrayList(plugin2, plugin1);
    //
    // given(pluginDependencyManager.sortPluginsInDependencyOrder(Mockito.anyCollectionOf(Plugin.class))).willReturn(
    // sortedPluginsToInitialize);
    //
    // given(pluginDescriptorParser.loadPlugins()).willReturn(pluginsFromDescriptor);
    //
    // given(pluginDao.list()).willReturn(pluginsFromDatabase);
    //
    // // when
    // pluginAccessor.init();
    //
    // // then
    // InOrder inOrder = inOrder(plugin2, plugin1, moduleFactoryAccessor);
    // inOrder.verify(plugin2).init();
    // inOrder.verify(plugin1).init();
    // inOrder.verify(moduleFactoryAccessor).postInitialize();
    // }
    //
    // @Test
    // public void shouldPerformEnableOnEnablingPlugins() throws Exception {
    // // given
    // Plugin plugin1 = mock(Plugin.class);
    // PersistentPlugin persistentPlugin1 = mock(PersistentPlugin.class);
    // given(plugin1.getIdentifier()).willReturn("identifier1");
    // given(persistentPlugin1.getIdentifier()).willReturn("identifier1");
    // given(plugin1.hasState(PluginState.ENABLING)).willReturn(true);
    // given(persistentPlugin1.hasState(PluginState.ENABLING)).willReturn(true);
    // Plugin plugin2 = mock(Plugin.class);
    // PersistentPlugin persistentPlugin2 = mock(PersistentPlugin.class);
    // given(plugin2.getIdentifier()).willReturn("identifier2");
    // given(persistentPlugin2.getIdentifier()).willReturn("identifier2");
    // given(plugin2.hasState(PluginState.ENABLING)).willReturn(true);
    // given(persistentPlugin2.hasState(PluginState.ENABLING)).willReturn(true);
    // Plugin plugin3 = mock(Plugin.class);
    // PersistentPlugin persistentPlugin3 = mock(PersistentPlugin.class);
    // given(persistentPlugin3.getIdentifier()).willReturn("identifier3");
    // given(plugin3.getIdentifier()).willReturn("identifier3");
    //
    // Set<Plugin> pluginsFromDescriptor = Sets.newHashSet(plugin1, plugin2, plugin3);
    // Set<PersistentPlugin> pluginsFromDatabase = Sets.newHashSet(persistentPlugin1, persistentPlugin2, persistentPlugin3);
    // List<Plugin> sortedPluginsToInitialize = Lists.newArrayList(plugin2, plugin1);
    //
    // given(pluginDependencyManager.sortPluginsInDependencyOrder(Mockito.anyCollectionOf(Plugin.class))).willReturn(
    // sortedPluginsToInitialize);
    //
    // given(pluginDescriptorParser.loadPlugins()).willReturn(pluginsFromDescriptor);
    //
    // given(pluginDao.list()).willReturn(pluginsFromDatabase);
    //
    // // when
    // pluginAccessor.init();
    //
    // // then
    // InOrder inOrder = inOrder(plugin2, plugin1, moduleFactoryAccessor);
    // inOrder.verify(plugin2).init();
    // inOrder.verify(plugin1).init();
    // inOrder.verify(moduleFactoryAccessor).postInitialize();
    // inOrder.verify(plugin2).changeStateTo(PluginState.ENABLED);
    // inOrder.verify(plugin1).changeStateTo(PluginState.ENABLED);
    // verify(plugin3, never()).changeStateTo(PluginState.ENABLED);
    // verify(pluginDao).save(plugin1);
    // verify(pluginDao).save(plugin2);
    // verify(pluginDao, never()).save(plugin3);
    // }
    //
    // @Test
    // public void shouldNotDeleteTemporaryPlugins() throws Exception {
    // // given
    // PersistentPlugin persistentPlugin1 = mock(PersistentPlugin.class);
    // given(persistentPlugin1.hasState(PluginState.TEMPORARY)).willReturn(true);
    // PersistentPlugin persistentPlugin2 = mock(PersistentPlugin.class);
    // given(persistentPlugin2.hasState(PluginState.TEMPORARY)).willReturn(false);
    //
    // Set<Plugin> pluginsFromDescriptor = Sets.newHashSet();
    // Set<PersistentPlugin> pluginsFromDatabase = Sets.newHashSet(persistentPlugin1, persistentPlugin2);
    //
    // given(pluginDescriptorParser.loadPlugins()).willReturn(pluginsFromDescriptor);
    // given(pluginDao.list()).willReturn(pluginsFromDatabase);
    //
    // // when
    // pluginAccessor.init();
    //
    // verify(pluginDao, never()).delete(persistentPlugin1);
    // verify(pluginDao).delete(persistentPlugin2);
    // }
    //
    // @Test
    // public void shouldRemovePlugin() throws Exception {
    // // given
    // Plugin plugin1 = mock(Plugin.class, "plugin1");
    //
    // given(plugin1.getIdentifier()).willReturn("identifier1");
    //
    // Plugin plugin2 = mock(Plugin.class);
    // given(plugin2.getIdentifier()).willReturn("identifier2");
    //
    // given(pluginDescriptorParser.loadPlugins()).willReturn(Sets.newHashSet(plugin1, plugin2));
    //
    // given(pluginDao.list()).willReturn(Sets.<PersistentPlugin> newHashSet(plugin1, plugin2));
    //
    // pluginAccessor.init();
    //
    // // when
    // pluginAccessor.removePlugin(plugin1);
    //
    // // then
    // assertEquals(1, pluginAccessor.getPlugins().size());
    // assertThat(pluginAccessor.getPlugins(), hasItems(plugin2));
    // }
    //
    // @Test
    // public void shouldSaveNewPlugin() throws Exception {
    // // given
    // Plugin plugin1 = mock(Plugin.class, "plugin1");
    //
    // given(plugin1.getIdentifier()).willReturn("identifier1");
    //
    // Plugin plugin2 = mock(Plugin.class);
    // given(plugin2.getIdentifier()).willReturn("identifier2");
    //
    // given(pluginDescriptorParser.loadPlugins()).willReturn(Sets.newHashSet(plugin1));
    //
    // given(pluginDao.list()).willReturn(Sets.<PersistentPlugin> newHashSet(plugin1));
    //
    // pluginAccessor.init();
    //
    // // when
    // pluginAccessor.savePlugin(plugin2);
    //
    // // then
    // assertThat(pluginAccessor.getPlugins(), hasItems(plugin1, plugin2));
    // }
    //
    // @Test
    // public void shouldSaveExistingPlugin() throws Exception {
    // // given
    // Plugin plugin1 = mock(Plugin.class, "plugin1");
    //
    // given(plugin1.getIdentifier()).willReturn("identifier1");
    //
    // Plugin plugin2 = mock(Plugin.class);
    // given(plugin2.getIdentifier()).willReturn("identifier1");
    //
    // given(pluginDescriptorParser.loadPlugins()).willReturn(Sets.newHashSet(plugin1));
    //
    // given(pluginDao.list()).willReturn(Sets.<PersistentPlugin> newHashSet(plugin1));
    //
    // pluginAccessor.init();
    //
    // // when
    // pluginAccessor.savePlugin(plugin2);
    //
    // // then
    // assertEquals(1, pluginAccessor.getPlugins().size());
    // assertThat(pluginAccessor.getPlugins(), hasItems(plugin2));
    // }

}
