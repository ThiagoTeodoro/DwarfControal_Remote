package br.com.datemanipulator;

/**
 * Enum para auxiliar a utilzação dos pattern de Data
 * 
 * Abreviações e patterns baseado na Código ISO639-2
 * 
 * @author Thiago Teodoro Rodrigues
 *
 */
public enum DatePatterns {
	
	//Código ISO639-2 por país 
	
	AL("Albania","yyyy-MM-dd"),
	AE("United Arab Emirates","dd/MM/yyyy"),
	AR("Argentina","dd/MM/yyyy"),
	AU("Australia","d/MM/yyyy"),
	AT("Austria","dd.MM.yyyy"),
	BE("Belgium","d/MM/yyyy"),	
	BG("Bulgaria","yyyy-M-d"),
	BH("Bahrain","dd/MM/yyyy"),
	BA("Bosnia and Herzegovina","yyyy-MM-dd"),
	BY("Belarus","d.M.yyyy"),
	BO("Bolivia","dd-MM-yyyy"),
	BR("Brazil","dd/MM/yyyy"),
	CA_F("Canada","yyyy-MM-dd"),
	CA_E("Canada","dd/MM/yyyy"),
	CH("Switzerland","dd.MM.yyyy"),
	CL("Chile","dd-MM-yyyy"),
	CN("China","yyyy-M-d"),
	CO("Colombia","d/MM/yyyy"),
	CR("Costa Rica","dd/MM/yyyy"),
	CY("Cyprus","dd/MM/yyyy"),
	CZ("Czech Republic","d.M.yyyy"),
	DE("Germany","dd.MM.yyyy"),
	DK("Denmark","dd-MM-yyyy"),
	DO("Dominican Republic","MM/dd/yyyy"),
	DZ("Algeria","dd/MM/yyyy"),
	EC("Ecuador","dd/MM/yyyy"),
	EG("Egypt","dd/MM/yyyy"),
	ES("Spain","d/MM/yyyy"),
	ES_C("Spain","dd/MM/yyyy"),
	EE("Estonia","d.MM.yyyy"),
	FI("Finland","d.M.yyyy"),
	FR("France","dd/MM/yyyy"),
	GB("United Kingdom","dd/MM/yyyy"),
	GR("Greece","d/M/yyyy"),
	GT("Guatemala","d/MM/yyyy"),
	HN("Honduras","MM-dd-yyyy"),
	HR("Croatia","dd.MM.yyyy."),
	HU("Hungary","yyyy.MM.dd."),
	ID("Indonesia","dd/MM/yyyy"),
	IN("India","d/M/yyyy"),
	IE_I("Ireland","dd/MM/yyyy"),
	IE_E("Ireland","dd/MM/yyyy"),
	IQ("Iraq","dd/MM/yyyy"),
	IS("Iceland","d.M.yyyy"),
	IL("Israel","dd/MM/yyyy"),
	IT("Italy","dd/MM/yyyy"),
	JO("Jordan","dd/MM/yyyy"),
	JP("Japan","yyyy/MM/dd"),	
	KR("South Korea","yyyy. M. d"),
	KW("Kuwait","dd/MM/yyyy"),
	LB("Lebanon","dd/MM/yyyy"),
	LY("Libya","dd/MM/yyyy"),
	LT("Lithuania","yyyy.M.d"),
	LU_F("Luxembourg","dd/MM/yyyy"),
	LU_G("Luxembourg","dd.MM.yyyy"),
	LV("Latvia","yyyy.d.M"),
	MA("Morocco","dd/MM/yyyy"),
	MX("Mexico","d/MM/yyyy"),
	MK("Macedonia","d.M.yyyy"),
	MT_E("Malta","dd/MM/yyyy"),
	MT_M("Malta","dd/MM/yyyy"),
	ME("Montenegro","d.M.yyyy."),
	MY("Malaysia","dd/MM/yyyy"),
	NI("Nicaragua","MM-dd-yyyy"),
	NL("Netherlands","d-M-yyyy"),
	NO("Norway","dd.MM.yyyy"),
	NZ("New Zealand","d/MM/yyyy"),
	OM("Oman","dd/MM/yyyy"),
	PA("Panama","MM/dd/yyyy"),
	PE("Peru","dd/MM/yyyy"),
	PH("Philippines","M/d/yyyy"),
	PL("Poland","dd.MM.yyyy"),
	PR("Puerto Rico","MM-dd-yyyy"),
	PT("Portugal","dd-MM-yyyy"),
	PY("Paraguay","dd/MM/yyyy"),
	QA("Qatar","dd/MM/yyyy"),
	RO("Romania","dd.MM.yyyy"),
	RU("Russia","dd.MM.yyyy"),
	SA("Saudi Arabia","dd/MM/yyyy"),
	CS("Serbia and Montenegro","d.M.yyyy."),
	SD("Sudan","dd/MM/yyyy"),
	SG_C("Singapore","dd/MM/yyyy"),
	SG_E("Singapore","M/d/yyyy"),
	SV("El Salvador","MM-dd-yyyy"),
	RS("Serbia","d.M.yyyy."),
	SK("Slovakia","d.M.yyyy"),
	SI("Slovenia","d.M.yyyy"),
	SE("Sweden","yyyy-MM-dd"),
	SY("Syria","dd/MM/yyyy"),
	TN("Tunisia","dd/MM/yyyy"),
	TR("Turkey","dd.MM.yyyy"),
	TW("Taiwan","yyyy/M/d"),
	UA("Ukraine","dd.MM.yyyy"),
	UY("Uruguay","dd/MM/yyyy"),
	US("United States","M/d/yyyy"),
	VE("Venezuela","dd/MM/yyyy"),
	VN("Vietnam","dd/MM/yyyy"),
	YE("Yemen","dd/MM/yyyy"),
	ZA("South Africa","yyyy/MM/dd"),
	
	
	//Pattern for SQL
	SQL("SQL - BD", "yyyy-MM-dd"),
	SQL_FULL("SQL_BD", "yyyy-MM-dd HH:mm:ss");
	
	
	//Campo ENUM
	public String country;
	public String pattern;
	
	//Construtor ENUM
	private DatePatterns(String country, String pattern) {
		this.country = country;
		this.pattern = pattern;
	}

}
