package org.uet.dse.rtlplus.mm;

public class MRule {
	
	private MPattern lhs;
	private MPattern rhs;

	public MRule(MPattern left, MPattern right) {
		lhs = left;
		rhs = right;
	}
	
}
