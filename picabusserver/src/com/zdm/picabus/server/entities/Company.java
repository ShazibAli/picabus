package com.zdm.picabus.server.entities;

public enum Company {
	EGGED("���"),
	EGGED_TAAVURA("��� ������"),
	DAN("��"),
	SHEEM("���"),
	NESIUT_AND_TAYARUT("������ �������"),
	G_B_TOURS("��.��.����"),
	OMNI_EXP("����� ������ "),
	ILLIT("�����"),
	NATEEV_EXPRESS("���� ������"),
	METROPOLIN("���������"),
	SUPERBUS("�������"),
	CONNEX("�����"),
	KAVIM("�����"),
	METRO_DAN("������"),
	GALIM("����"),
	MOAZA_EZ_GOLAN("����� ������ ����"),
	AFIKIM("������"),
	DAN_NORTH("�� �����"),
	EAST_JERUSALEM("���� �������"),
	UNKNOWN("Unknown");
	
	private String companyName;
	
	
	private Company (String companyName) {
		this.companyName = companyName;
	}
	
	public String getCompanyName() {
		return companyName;
	}
	
	@Override
	public String toString(){
		return companyName;
	}
	
	public static Company getCompanyByString (String companyName) {
		if (EGGED.getCompanyName().equalsIgnoreCase(companyName)) {
			return EGGED;
		} 
		else if (DAN.getCompanyName().equalsIgnoreCase(companyName)) {
			return DAN;
		}
		else if (METROPOLIN.getCompanyName().equalsIgnoreCase(companyName)) {
			return METROPOLIN;
		}
		else return UNKNOWN;
	}
}
