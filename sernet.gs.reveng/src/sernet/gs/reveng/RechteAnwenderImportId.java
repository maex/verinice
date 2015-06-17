package sernet.gs.reveng;

// Generated Jun 5, 2015 1:28:30 PM by Hibernate Tools 3.4.0.CR1

/**
 * RechteAnwenderImportId generated by hbm2java
 */
public class RechteAnwenderImportId implements java.io.Serializable {

	private short uid;
	private int impId;

	public RechteAnwenderImportId() {
	}

	public RechteAnwenderImportId(short uid, int impId) {
		this.uid = uid;
		this.impId = impId;
	}

	public short getUid() {
		return this.uid;
	}

	public void setUid(short uid) {
		this.uid = uid;
	}

	public int getImpId() {
		return this.impId;
	}

	public void setImpId(int impId) {
		this.impId = impId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof RechteAnwenderImportId))
			return false;
		RechteAnwenderImportId castOther = (RechteAnwenderImportId) other;

		return (this.getUid() == castOther.getUid())
				&& (this.getImpId() == castOther.getImpId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getUid();
		result = 37 * result + this.getImpId();
		return result;
	}

}