package sernet.gs.reveng;

// Generated Jun 5, 2015 1:28:30 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * NZobEsa generated by hbm2java
 */
public class NZobEsa implements java.io.Serializable {

	private NZobEsaId id;
	private MsUnj msUnj;
	private NZielobjekt NZielobjekt;
	private MYesno MYesno;
	private byte esaEinsatz;
	private byte esaModellierung;
	private String esaSzenario;
	private Date esaDatumErfasst;
	private Integer esaZobIdMit;
	private String esaEntscheidDurch;
	private String esaEntscheidEingetragen;
	private Date esaEntscheidDatum;
	private Date esaRaDatumBis;
	private String esaBegruendung;
	private String esaLink;
	private Byte impNeu;
	private String guid;
	private String guidOrg;
	private int usn;

	public NZobEsa() {
	}

	public NZobEsa(MsUnj msUnj, NZielobjekt NZielobjekt, MYesno MYesno,
			byte esaEinsatz, byte esaModellierung, String esaSzenario,
			String esaBegruendung, String esaLink, String guid, int usn) {
		this.msUnj = msUnj;
		this.NZielobjekt = NZielobjekt;
		this.MYesno = MYesno;
		this.esaEinsatz = esaEinsatz;
		this.esaModellierung = esaModellierung;
		this.esaSzenario = esaSzenario;
		this.esaBegruendung = esaBegruendung;
		this.esaLink = esaLink;
		this.guid = guid;
		this.usn = usn;
	}

	public NZobEsa(MsUnj msUnj, NZielobjekt NZielobjekt, MYesno MYesno,
			byte esaEinsatz, byte esaModellierung, String esaSzenario,
			Date esaDatumErfasst, Integer esaZobIdMit,
			String esaEntscheidDurch, String esaEntscheidEingetragen,
			Date esaEntscheidDatum, Date esaRaDatumBis, String esaBegruendung,
			String esaLink, Byte impNeu, String guid, String guidOrg, int usn) {
		this.msUnj = msUnj;
		this.NZielobjekt = NZielobjekt;
		this.MYesno = MYesno;
		this.esaEinsatz = esaEinsatz;
		this.esaModellierung = esaModellierung;
		this.esaSzenario = esaSzenario;
		this.esaDatumErfasst = esaDatumErfasst;
		this.esaZobIdMit = esaZobIdMit;
		this.esaEntscheidDurch = esaEntscheidDurch;
		this.esaEntscheidEingetragen = esaEntscheidEingetragen;
		this.esaEntscheidDatum = esaEntscheidDatum;
		this.esaRaDatumBis = esaRaDatumBis;
		this.esaBegruendung = esaBegruendung;
		this.esaLink = esaLink;
		this.impNeu = impNeu;
		this.guid = guid;
		this.guidOrg = guidOrg;
		this.usn = usn;
	}

	public NZobEsaId getId() {
		return this.id;
	}

	public void setId(NZobEsaId id) {
		this.id = id;
	}

	public MsUnj getMsUnj() {
		return this.msUnj;
	}

	public void setMsUnj(MsUnj msUnj) {
		this.msUnj = msUnj;
	}

	public NZielobjekt getNZielobjekt() {
		return this.NZielobjekt;
	}

	public void setNZielobjekt(NZielobjekt NZielobjekt) {
		this.NZielobjekt = NZielobjekt;
	}

	public MYesno getMYesno() {
		return this.MYesno;
	}

	public void setMYesno(MYesno MYesno) {
		this.MYesno = MYesno;
	}

	public byte getEsaEinsatz() {
		return this.esaEinsatz;
	}

	public void setEsaEinsatz(byte esaEinsatz) {
		this.esaEinsatz = esaEinsatz;
	}

	public byte getEsaModellierung() {
		return this.esaModellierung;
	}

	public void setEsaModellierung(byte esaModellierung) {
		this.esaModellierung = esaModellierung;
	}

	public String getEsaSzenario() {
		return this.esaSzenario;
	}

	public void setEsaSzenario(String esaSzenario) {
		this.esaSzenario = esaSzenario;
	}

	public Date getEsaDatumErfasst() {
		return this.esaDatumErfasst;
	}

	public void setEsaDatumErfasst(Date esaDatumErfasst) {
		this.esaDatumErfasst = esaDatumErfasst;
	}

	public Integer getEsaZobIdMit() {
		return this.esaZobIdMit;
	}

	public void setEsaZobIdMit(Integer esaZobIdMit) {
		this.esaZobIdMit = esaZobIdMit;
	}

	public String getEsaEntscheidDurch() {
		return this.esaEntscheidDurch;
	}

	public void setEsaEntscheidDurch(String esaEntscheidDurch) {
		this.esaEntscheidDurch = esaEntscheidDurch;
	}

	public String getEsaEntscheidEingetragen() {
		return this.esaEntscheidEingetragen;
	}

	public void setEsaEntscheidEingetragen(String esaEntscheidEingetragen) {
		this.esaEntscheidEingetragen = esaEntscheidEingetragen;
	}

	public Date getEsaEntscheidDatum() {
		return this.esaEntscheidDatum;
	}

	public void setEsaEntscheidDatum(Date esaEntscheidDatum) {
		this.esaEntscheidDatum = esaEntscheidDatum;
	}

	public Date getEsaRaDatumBis() {
		return this.esaRaDatumBis;
	}

	public void setEsaRaDatumBis(Date esaRaDatumBis) {
		this.esaRaDatumBis = esaRaDatumBis;
	}

	public String getEsaBegruendung() {
		return this.esaBegruendung;
	}

	public void setEsaBegruendung(String esaBegruendung) {
		this.esaBegruendung = esaBegruendung;
	}

	public String getEsaLink() {
		return this.esaLink;
	}

	public void setEsaLink(String esaLink) {
		this.esaLink = esaLink;
	}

	public Byte getImpNeu() {
		return this.impNeu;
	}

	public void setImpNeu(Byte impNeu) {
		this.impNeu = impNeu;
	}

	public String getGuid() {
		return this.guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getGuidOrg() {
		return this.guidOrg;
	}

	public void setGuidOrg(String guidOrg) {
		this.guidOrg = guidOrg;
	}

	public int getUsn() {
		return this.usn;
	}

	public void setUsn(int usn) {
		this.usn = usn;
	}

}