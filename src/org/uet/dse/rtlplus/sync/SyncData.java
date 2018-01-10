package org.uet.dse.rtlplus.sync;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.tzi.use.uml.sys.MObject;
import org.uet.dse.rtlplus.mm.MRuleCollection;
import org.uet.dse.rtlplus.mm.MTggRule;

public class SyncData {
	private Map<String, Set<MTggRule>> rulesForSrcClass;
	private Map<String, Set<MTggRule>> rulesForTrgClass;
	private Map<String, Set<String>> forwardCmdsForCorr;
	private Map<String, Set<String>> backwardCmdsForCorr;
	private Map<String, Set<String>> corrObjsForSrc;
	private Map<String, Set<String>> corrObjsForTrg;

	public SyncData(MRuleCollection rules) {
		rulesForSrcClass = new HashMap<>();
		rulesForTrgClass = new HashMap<>();
		forwardCmdsForCorr = new HashMap<>();
		backwardCmdsForCorr = new HashMap<>();
		corrObjsForSrc = new HashMap<>();
		corrObjsForTrg = new HashMap<>();
		for (MTggRule tggRule : rules.getRuleList()) {
			for (MObject obj : tggRule.getSrcRule().getNewObjects()) {
				Set<MTggRule> tggRules = rulesForSrcClass.get(obj.cls().name());
				if (tggRules == null) {
					tggRules = new HashSet<>();
					rulesForSrcClass.put(obj.cls().name(), tggRules);
				}
				tggRules.add(tggRule);

			}
			for (MObject obj : tggRule.getTrgRule().getNewObjects()) {
				Set<MTggRule> tggRules = rulesForSrcClass.get(obj.cls().name());
				if (tggRules == null) {
					tggRules = new HashSet<>();
					rulesForTrgClass.put(obj.cls().name(), tggRules);
				}
				tggRules.add(tggRule);

			}
			for (Map.Entry<String, List<String>> entry : tggRule.getCorrRule().getRhs().getInvariantList().entrySet()) {
				String cls = entry.getKey();
				List<String> invs = entry.getValue();
				Set<String> fCmds = forwardCmdsForCorr.get(cls);
				if (fCmds == null) {
					fCmds = new HashSet<>(2);
					forwardCmdsForCorr.put(cls, fCmds);
				}
				Set<String> bCmds = backwardCmdsForCorr.get(cls);
				if (bCmds == null) {
					bCmds = new HashSet<>(2);
					backwardCmdsForCorr.put(cls, bCmds);
				}
				for (String inv : invs) {
					fCmds.add(inv.replace("=", ":="));
					String[] parts = inv.split("=");
					bCmds.add(parts[1] + ":=" + parts[0]);
				}
			}
		}
//		System.out.println(forwardCmdsForCorr.toString());
//		System.out.println("=============================");
//		System.out.println(backwardCmdsForCorr.toString());
	}

	public Map<String, Set<MTggRule>> getRulesForSrcClass() {
		return rulesForSrcClass;
	}

	public Map<String, Set<MTggRule>> getRulesForTrgClass() {
		return rulesForTrgClass;
	}

	public Map<String, Set<String>> getForwardCmdsForCorr() {
		return forwardCmdsForCorr;
	}

	public Map<String, Set<String>> getBackwardCmdsForCorr() {
		return backwardCmdsForCorr;
	}

	public void addSrcCorrLink(String src, String corr) {
		Set<String> corrs = corrObjsForSrc.get(src);
		if (corrs == null) {
			corrs = new HashSet<>();
			corrObjsForSrc.put(src, corrs);
		}
		corrs.add(corr);
	}

	public void addTrgCorrLink(String trg, String corr) {
		Set<String> corrs = corrObjsForTrg.get(trg);
		if (corrs == null) {
			corrs = new HashSet<>();
			corrObjsForTrg.put(trg, corrs);
		}
		corrs.add(corr);
	}

	public Map<String, Set<String>> getCorrObjsForSrc() {
		return corrObjsForSrc;
	}

	public Map<String, Set<String>> getCorrObjsForTrg() {
		return corrObjsForTrg;
	}
	

}
