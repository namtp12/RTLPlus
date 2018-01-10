package org.uet.dse.rtlplus;

import org.tzi.use.runtime.IPlugin;
import org.tzi.use.runtime.IPluginRuntime;
import org.tzi.use.util.UniqueNameGenerator;
import org.uet.dse.rtlplus.mm.MRuleCollection;
import org.uet.dse.rtlplus.mm.MRuleCollection.TransformationType;
import org.uet.dse.rtlplus.sync.SyncData;

public class Main implements IPlugin {
	@Override
	public String getName() {
		return "RTL plugin";
	}

	@Override
	public void run(IPluginRuntime pluginRuntime) throws Exception {
	}

	private static MRuleCollection fTggRules = new MRuleCollection(TransformationType.FORWARD);
	private static UniqueNameGenerator fUniqueNameGenerator;
	private static SyncData syncData;

	public static MRuleCollection getTggRuleCollection() {
		return fTggRules;
	}

	public static void setRTLRule(MRuleCollection rules) {
		fTggRules = rules;
		fUniqueNameGenerator = new UniqueNameGenerator();
		syncData = new SyncData(rules);
	}

	public static UniqueNameGenerator getUniqueNameGenerator() {
		return fUniqueNameGenerator;
	}
	
	public static SyncData getSyncData() {
		return syncData;
	}

}
