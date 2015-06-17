package sernet.gs.reveng;

// Generated Jun 5, 2015 1:28:30 PM by Hibernate Tools 3.4.0.CR1

/**
 * SysconstraintsId generated by hbm2java
 */
public class SysconstraintsId implements java.io.Serializable {

	private Integer constid;
	private Integer id;
	private Short colid;
	private Byte spare1;
	private Integer status;
	private Integer actions;
	private Integer error;

	public SysconstraintsId() {
	}

	public SysconstraintsId(Integer constid, Integer id, Short colid,
			Byte spare1, Integer status, Integer actions, Integer error) {
		this.constid = constid;
		this.id = id;
		this.colid = colid;
		this.spare1 = spare1;
		this.status = status;
		this.actions = actions;
		this.error = error;
	}

	public Integer getConstid() {
		return this.constid;
	}

	public void setConstid(Integer constid) {
		this.constid = constid;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Short getColid() {
		return this.colid;
	}

	public void setColid(Short colid) {
		this.colid = colid;
	}

	public Byte getSpare1() {
		return this.spare1;
	}

	public void setSpare1(Byte spare1) {
		this.spare1 = spare1;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getActions() {
		return this.actions;
	}

	public void setActions(Integer actions) {
		this.actions = actions;
	}

	public Integer getError() {
		return this.error;
	}

	public void setError(Integer error) {
		this.error = error;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SysconstraintsId))
			return false;
		SysconstraintsId castOther = (SysconstraintsId) other;

		return ((this.getConstid() == castOther.getConstid()) || (this
				.getConstid() != null && castOther.getConstid() != null && this
				.getConstid().equals(castOther.getConstid())))
				&& ((this.getId() == castOther.getId()) || (this.getId() != null
						&& castOther.getId() != null && this.getId().equals(
						castOther.getId())))
				&& ((this.getColid() == castOther.getColid()) || (this
						.getColid() != null && castOther.getColid() != null && this
						.getColid().equals(castOther.getColid())))
				&& ((this.getSpare1() == castOther.getSpare1()) || (this
						.getSpare1() != null && castOther.getSpare1() != null && this
						.getSpare1().equals(castOther.getSpare1())))
				&& ((this.getStatus() == castOther.getStatus()) || (this
						.getStatus() != null && castOther.getStatus() != null && this
						.getStatus().equals(castOther.getStatus())))
				&& ((this.getActions() == castOther.getActions()) || (this
						.getActions() != null && castOther.getActions() != null && this
						.getActions().equals(castOther.getActions())))
				&& ((this.getError() == castOther.getError()) || (this
						.getError() != null && castOther.getError() != null && this
						.getError().equals(castOther.getError())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getConstid() == null ? 0 : this.getConstid().hashCode());
		result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());
		result = 37 * result
				+ (getColid() == null ? 0 : this.getColid().hashCode());
		result = 37 * result
				+ (getSpare1() == null ? 0 : this.getSpare1().hashCode());
		result = 37 * result
				+ (getStatus() == null ? 0 : this.getStatus().hashCode());
		result = 37 * result
				+ (getActions() == null ? 0 : this.getActions().hashCode());
		result = 37 * result
				+ (getError() == null ? 0 : this.getError().hashCode());
		return result;
	}

}