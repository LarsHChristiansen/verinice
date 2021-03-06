package sernet.gs.reveng;

// Generated Jun 5, 2015 1:28:30 PM by Hibernate Tools 3.4.0.CR1

/**
 * TmpZotSelId generated by hbm2java
 */
public class TmpZotSelId implements java.io.Serializable {

	private short spid;
	private int zotImpId;
	private int zotId;

	public TmpZotSelId() {
	}

	public TmpZotSelId(short spid, int zotImpId, int zotId) {
		this.spid = spid;
		this.zotImpId = zotImpId;
		this.zotId = zotId;
	}

	public short getSpid() {
		return this.spid;
	}

	public void setSpid(short spid) {
		this.spid = spid;
	}

	public int getZotImpId() {
		return this.zotImpId;
	}

	public void setZotImpId(int zotImpId) {
		this.zotImpId = zotImpId;
	}

	public int getZotId() {
		return this.zotId;
	}

	public void setZotId(int zotId) {
		this.zotId = zotId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TmpZotSelId))
			return false;
		TmpZotSelId castOther = (TmpZotSelId) other;

		return (this.getSpid() == castOther.getSpid())
				&& (this.getZotImpId() == castOther.getZotImpId())
				&& (this.getZotId() == castOther.getZotId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getSpid();
		result = 37 * result + this.getZotImpId();
		result = 37 * result + this.getZotId();
		return result;
	}

}
