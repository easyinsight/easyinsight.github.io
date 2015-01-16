
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CurrencyLocale.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CurrencyLocale">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_afghanistanPashto"/>
 *     &lt;enumeration value="_afghanistanPersian"/>
 *     &lt;enumeration value="_alandIslandsSwedish"/>
 *     &lt;enumeration value="_albaniaAlbanian"/>
 *     &lt;enumeration value="_algeriaArabic"/>
 *     &lt;enumeration value="_angolaPortuguese"/>
 *     &lt;enumeration value="_anguillaEnglish"/>
 *     &lt;enumeration value="_antiguaAndBarbudaEnglish"/>
 *     &lt;enumeration value="_argentinaSpanish"/>
 *     &lt;enumeration value="_armeniaArmenian"/>
 *     &lt;enumeration value="_arubaEnglish"/>
 *     &lt;enumeration value="_arubaPortuguese"/>
 *     &lt;enumeration value="_australiaEnglish"/>
 *     &lt;enumeration value="_austriaGerman"/>
 *     &lt;enumeration value="_azerbaijanAzerbaijani"/>
 *     &lt;enumeration value="_bahamasEnglish"/>
 *     &lt;enumeration value="_bahrainArabic"/>
 *     &lt;enumeration value="_barbadosEnglish"/>
 *     &lt;enumeration value="_belarusByelorussian"/>
 *     &lt;enumeration value="_belgiumDutch"/>
 *     &lt;enumeration value="_belgiumFrench"/>
 *     &lt;enumeration value="_belizeEnglish"/>
 *     &lt;enumeration value="_bengali"/>
 *     &lt;enumeration value="_beninFrench"/>
 *     &lt;enumeration value="_bermudaEnglish"/>
 *     &lt;enumeration value="_bhutanDzongkha"/>
 *     &lt;enumeration value="_boliviaSpanish"/>
 *     &lt;enumeration value="_bonaireSaintEustatiusAndSabaDutch"/>
 *     &lt;enumeration value="_bosniaAndHerzegovinaBosnian"/>
 *     &lt;enumeration value="_botswanaEnglish"/>
 *     &lt;enumeration value="_brazilPortuguese"/>
 *     &lt;enumeration value="_bruneiMalay"/>
 *     &lt;enumeration value="_bulgariaBulgarian"/>
 *     &lt;enumeration value="_burkinaFasoFrench"/>
 *     &lt;enumeration value="_burundiFrench"/>
 *     &lt;enumeration value="_cambodiaKhmer"/>
 *     &lt;enumeration value="_cameroonFrench"/>
 *     &lt;enumeration value="_canadaEnglish"/>
 *     &lt;enumeration value="_canadaFrench"/>
 *     &lt;enumeration value="_canaryIslandsSpanish"/>
 *     &lt;enumeration value="_capeVerdePortuguese"/>
 *     &lt;enumeration value="_caymanIslandsEnglish"/>
 *     &lt;enumeration value="_centralAfricanRepublicFrench"/>
 *     &lt;enumeration value="_ceutaAndMelillaSpanish"/>
 *     &lt;enumeration value="_chadFrench"/>
 *     &lt;enumeration value="_chileSpanish"/>
 *     &lt;enumeration value="_chinaChinese"/>
 *     &lt;enumeration value="_colombiaSpanish"/>
 *     &lt;enumeration value="_comorosFrench"/>
 *     &lt;enumeration value="_congoDemocraticRepublicEnglish"/>
 *     &lt;enumeration value="_congoDemocraticRepublicFrench"/>
 *     &lt;enumeration value="_congoRepublicOfFrench"/>
 *     &lt;enumeration value="_costaRicaSpanish"/>
 *     &lt;enumeration value="_coteDivoireFrench"/>
 *     &lt;enumeration value="_croatiaCroatian"/>
 *     &lt;enumeration value="_cubaSpanish"/>
 *     &lt;enumeration value="_curacaoDutch"/>
 *     &lt;enumeration value="_cyprusEnglish"/>
 *     &lt;enumeration value="_cyprusEnglishEuro"/>
 *     &lt;enumeration value="_czechRepublicCzech"/>
 *     &lt;enumeration value="_denmarkDanish"/>
 *     &lt;enumeration value="_djiboutiArabic"/>
 *     &lt;enumeration value="_djiboutiFrench"/>
 *     &lt;enumeration value="_dominicaEnglish"/>
 *     &lt;enumeration value="_dominicanRepublicSpanish"/>
 *     &lt;enumeration value="_ecuadorSpanish"/>
 *     &lt;enumeration value="_egyptArabic"/>
 *     &lt;enumeration value="_elSalvadorSpanish"/>
 *     &lt;enumeration value="_equatorialGuineaSpanish"/>
 *     &lt;enumeration value="_eritreaAfar"/>
 *     &lt;enumeration value="_estoniaEstonian"/>
 *     &lt;enumeration value="_ethiopiaAmharic"/>
 *     &lt;enumeration value="_falklandIslandsEnglish"/>
 *     &lt;enumeration value="_fijiFijian"/>
 *     &lt;enumeration value="_finlandFinnish"/>
 *     &lt;enumeration value="_finlandFinnishEuro"/>
 *     &lt;enumeration value="_franceFrench"/>
 *     &lt;enumeration value="_franceFrenchEuro"/>
 *     &lt;enumeration value="_frenchPolynesiaFrench"/>
 *     &lt;enumeration value="_gabonFrench"/>
 *     &lt;enumeration value="_gambiaEnglish"/>
 *     &lt;enumeration value="_georgiaGeorgian"/>
 *     &lt;enumeration value="_germanyGerman"/>
 *     &lt;enumeration value="_germanyGermanEuro"/>
 *     &lt;enumeration value="_ghanaEnglish"/>
 *     &lt;enumeration value="_gibraltarEnglish"/>
 *     &lt;enumeration value="_goldOunce"/>
 *     &lt;enumeration value="_greeceGreek"/>
 *     &lt;enumeration value="_grenadaEnglish"/>
 *     &lt;enumeration value="_guatemalaSpanish"/>
 *     &lt;enumeration value="_guineaBissauPortuguese"/>
 *     &lt;enumeration value="_guineaFrench"/>
 *     &lt;enumeration value="_guyanaEnglish"/>
 *     &lt;enumeration value="_haitian"/>
 *     &lt;enumeration value="_hondurasSpanish"/>
 *     &lt;enumeration value="_hongKongChinese"/>
 *     &lt;enumeration value="_hungaryHungarian"/>
 *     &lt;enumeration value="_icelandIcelandic"/>
 *     &lt;enumeration value="_indiaEnglish"/>
 *     &lt;enumeration value="_indiaGujarati"/>
 *     &lt;enumeration value="_indiaHindi"/>
 *     &lt;enumeration value="_indiaKannada"/>
 *     &lt;enumeration value="_indiaMarathi"/>
 *     &lt;enumeration value="_indiaPanjabi"/>
 *     &lt;enumeration value="_indiaTamil"/>
 *     &lt;enumeration value="_indiaTelugu"/>
 *     &lt;enumeration value="_indonesiaIndonesian"/>
 *     &lt;enumeration value="_iranPersian"/>
 *     &lt;enumeration value="_iraqArabic"/>
 *     &lt;enumeration value="_irelandEnglish"/>
 *     &lt;enumeration value="_israelHebrew"/>
 *     &lt;enumeration value="_italyItalian"/>
 *     &lt;enumeration value="_italyItalianEuro"/>
 *     &lt;enumeration value="_jamaicaEnglish"/>
 *     &lt;enumeration value="_japanJapanese"/>
 *     &lt;enumeration value="_jordanArabic"/>
 *     &lt;enumeration value="_jordanEnglish"/>
 *     &lt;enumeration value="_kazakhstanRussian"/>
 *     &lt;enumeration value="_kenyaEnglish"/>
 *     &lt;enumeration value="_kuwaitArabic"/>
 *     &lt;enumeration value="_kuwaitEnglish"/>
 *     &lt;enumeration value="_kyrgyzstanRussian"/>
 *     &lt;enumeration value="_laosLao"/>
 *     &lt;enumeration value="_latviaLatvianLettish"/>
 *     &lt;enumeration value="_lebanonArabic"/>
 *     &lt;enumeration value="_lesothoEnglish"/>
 *     &lt;enumeration value="_liberiaEnglish"/>
 *     &lt;enumeration value="_libyaArabic"/>
 *     &lt;enumeration value="_lithuaniaLithuanian"/>
 *     &lt;enumeration value="_luxembourgFrench"/>
 *     &lt;enumeration value="_luxembourgGerman"/>
 *     &lt;enumeration value="_luxembourgLuxembourgish"/>
 *     &lt;enumeration value="_macauChinese"/>
 *     &lt;enumeration value="_macedoniaMacedonian"/>
 *     &lt;enumeration value="_malawiEnglish"/>
 *     &lt;enumeration value="_malaysiaMalay"/>
 *     &lt;enumeration value="_maldivesDhivehi"/>
 *     &lt;enumeration value="_maliFrench"/>
 *     &lt;enumeration value="_mauritiusEnglish"/>
 *     &lt;enumeration value="_mexicoSpanish"/>
 *     &lt;enumeration value="_moldovaRomanian"/>
 *     &lt;enumeration value="_moldovaRussian"/>
 *     &lt;enumeration value="_mongoliaMongolian"/>
 *     &lt;enumeration value="_moroccoArabic"/>
 *     &lt;enumeration value="_mozambiquePortuguese"/>
 *     &lt;enumeration value="_myanmarBurmese"/>
 *     &lt;enumeration value="_namibiaEnglish"/>
 *     &lt;enumeration value="_nepalNepali"/>
 *     &lt;enumeration value="_netherlandsAntillesDutch"/>
 *     &lt;enumeration value="_netherlandsDutch"/>
 *     &lt;enumeration value="_netherlandsDutchEuro"/>
 *     &lt;enumeration value="_newCaledoniaFrench"/>
 *     &lt;enumeration value="_newZealandEnglish"/>
 *     &lt;enumeration value="_nicaraguaSpanish"/>
 *     &lt;enumeration value="_nigerFrench"/>
 *     &lt;enumeration value="_nigeriaEnglish"/>
 *     &lt;enumeration value="_northKoreaKorean"/>
 *     &lt;enumeration value="_norwayNorwegian"/>
 *     &lt;enumeration value="_omanArabic"/>
 *     &lt;enumeration value="_pakistanUrdu"/>
 *     &lt;enumeration value="_palladiumOunce"/>
 *     &lt;enumeration value="_panamaSpanish"/>
 *     &lt;enumeration value="_papuaNewGuineaEnglish"/>
 *     &lt;enumeration value="_paraguaySpanish"/>
 *     &lt;enumeration value="_peruSpanish"/>
 *     &lt;enumeration value="_philippinesEnglish"/>
 *     &lt;enumeration value="_philippinesTagalog"/>
 *     &lt;enumeration value="_platinumOunce"/>
 *     &lt;enumeration value="_polandPolish"/>
 *     &lt;enumeration value="_portugalPortuguese"/>
 *     &lt;enumeration value="_portugalPortugueseEuro"/>
 *     &lt;enumeration value="_puertoRicoSpanish"/>
 *     &lt;enumeration value="_qatarArabic"/>
 *     &lt;enumeration value="_qatarEnglish"/>
 *     &lt;enumeration value="_romaniaRomanian"/>
 *     &lt;enumeration value="_russiaRussian"/>
 *     &lt;enumeration value="_rwandaFrench"/>
 *     &lt;enumeration value="_saintBarthelemyFrench"/>
 *     &lt;enumeration value="_saintHelenaEnglish"/>
 *     &lt;enumeration value="_saintKittsAndNevisEnglish"/>
 *     &lt;enumeration value="_saintLuciaEnglish"/>
 *     &lt;enumeration value="_saintMartinEnglish"/>
 *     &lt;enumeration value="_saintVincentAndTheGrenadinesEnglish"/>
 *     &lt;enumeration value="_samoaSamoan"/>
 *     &lt;enumeration value="_saoTomeAndPrincipePortuguese"/>
 *     &lt;enumeration value="_saudiArabiaArabic"/>
 *     &lt;enumeration value="_senegalFrench"/>
 *     &lt;enumeration value="_serbiaAndMontenegroSerbian"/>
 *     &lt;enumeration value="_serbiaSerbian"/>
 *     &lt;enumeration value="_serbiaSerboCroatian"/>
 *     &lt;enumeration value="_seychellesEnglish"/>
 *     &lt;enumeration value="_seychellesFrench"/>
 *     &lt;enumeration value="_sierraLeoneEnglish"/>
 *     &lt;enumeration value="_silverOunce"/>
 *     &lt;enumeration value="_singaporeEnglish"/>
 *     &lt;enumeration value="_sintMaartenDutch"/>
 *     &lt;enumeration value="_slovakiaSlovak"/>
 *     &lt;enumeration value="_slovakiaSlovakEuro"/>
 *     &lt;enumeration value="_sloveniaSlovenian"/>
 *     &lt;enumeration value="_sloveniaSlovenianEuro"/>
 *     &lt;enumeration value="_solomonIslandsEnglish"/>
 *     &lt;enumeration value="_somaliaSomali"/>
 *     &lt;enumeration value="_southAfricaAfrikaans"/>
 *     &lt;enumeration value="_southAfricaEnglish"/>
 *     &lt;enumeration value="_southKoreaKorean"/>
 *     &lt;enumeration value="_southSudanEnglish"/>
 *     &lt;enumeration value="_spainCatalan"/>
 *     &lt;enumeration value="_spainSpanish"/>
 *     &lt;enumeration value="_spainSpanishEuro"/>
 *     &lt;enumeration value="_sriLankaSinhalese"/>
 *     &lt;enumeration value="_sudanArabic"/>
 *     &lt;enumeration value="_surinameDutch"/>
 *     &lt;enumeration value="_swazilandSwati"/>
 *     &lt;enumeration value="_swedenSwedish"/>
 *     &lt;enumeration value="_switzerlandFrench"/>
 *     &lt;enumeration value="_switzerlandGerman"/>
 *     &lt;enumeration value="_switzerlandItalian"/>
 *     &lt;enumeration value="_syriaArabic"/>
 *     &lt;enumeration value="_taiwanChinese"/>
 *     &lt;enumeration value="_tajikistanTajik"/>
 *     &lt;enumeration value="_tanzaniaEnglish"/>
 *     &lt;enumeration value="_thailandThai"/>
 *     &lt;enumeration value="_togoFrench"/>
 *     &lt;enumeration value="_tongaTonga"/>
 *     &lt;enumeration value="_trinidadAndTobagoEnglish"/>
 *     &lt;enumeration value="_tunisiaArabic"/>
 *     &lt;enumeration value="_turkeyTurkish"/>
 *     &lt;enumeration value="_turkmenistanTurkmen"/>
 *     &lt;enumeration value="_turksAndCaicosIslandsEnglish"/>
 *     &lt;enumeration value="_ugandaEnglish"/>
 *     &lt;enumeration value="_ukraineUkrainian"/>
 *     &lt;enumeration value="_unitedArabEmiratesArabic"/>
 *     &lt;enumeration value="_unitedArabEmiratesEnglish"/>
 *     &lt;enumeration value="_unitedKingdomEnglish"/>
 *     &lt;enumeration value="_unitedStatesEnglish"/>
 *     &lt;enumeration value="_uruguaySpanish"/>
 *     &lt;enumeration value="_uzbekistanUzbek"/>
 *     &lt;enumeration value="_vanuatuEnglish"/>
 *     &lt;enumeration value="_vanuatuFrench"/>
 *     &lt;enumeration value="_venezuelaSpanish"/>
 *     &lt;enumeration value="_vietnamVietnamese"/>
 *     &lt;enumeration value="_wallisAndFutunaFrench"/>
 *     &lt;enumeration value="_yemenArabic"/>
 *     &lt;enumeration value="_zambiaEnglish"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CurrencyLocale", namespace = "urn:types.accounting_2014_1.lists.webservices.netsuite.com")
@XmlEnum
public enum CurrencyLocale {

    @XmlEnumValue("_afghanistanPashto")
    AFGHANISTAN_PASHTO("_afghanistanPashto"),
    @XmlEnumValue("_afghanistanPersian")
    AFGHANISTAN_PERSIAN("_afghanistanPersian"),
    @XmlEnumValue("_alandIslandsSwedish")
    ALAND_ISLANDS_SWEDISH("_alandIslandsSwedish"),
    @XmlEnumValue("_albaniaAlbanian")
    ALBANIA_ALBANIAN("_albaniaAlbanian"),
    @XmlEnumValue("_algeriaArabic")
    ALGERIA_ARABIC("_algeriaArabic"),
    @XmlEnumValue("_angolaPortuguese")
    ANGOLA_PORTUGUESE("_angolaPortuguese"),
    @XmlEnumValue("_anguillaEnglish")
    ANGUILLA_ENGLISH("_anguillaEnglish"),
    @XmlEnumValue("_antiguaAndBarbudaEnglish")
    ANTIGUA_AND_BARBUDA_ENGLISH("_antiguaAndBarbudaEnglish"),
    @XmlEnumValue("_argentinaSpanish")
    ARGENTINA_SPANISH("_argentinaSpanish"),
    @XmlEnumValue("_armeniaArmenian")
    ARMENIA_ARMENIAN("_armeniaArmenian"),
    @XmlEnumValue("_arubaEnglish")
    ARUBA_ENGLISH("_arubaEnglish"),
    @XmlEnumValue("_arubaPortuguese")
    ARUBA_PORTUGUESE("_arubaPortuguese"),
    @XmlEnumValue("_australiaEnglish")
    AUSTRALIA_ENGLISH("_australiaEnglish"),
    @XmlEnumValue("_austriaGerman")
    AUSTRIA_GERMAN("_austriaGerman"),
    @XmlEnumValue("_azerbaijanAzerbaijani")
    AZERBAIJAN_AZERBAIJANI("_azerbaijanAzerbaijani"),
    @XmlEnumValue("_bahamasEnglish")
    BAHAMAS_ENGLISH("_bahamasEnglish"),
    @XmlEnumValue("_bahrainArabic")
    BAHRAIN_ARABIC("_bahrainArabic"),
    @XmlEnumValue("_barbadosEnglish")
    BARBADOS_ENGLISH("_barbadosEnglish"),
    @XmlEnumValue("_belarusByelorussian")
    BELARUS_BYELORUSSIAN("_belarusByelorussian"),
    @XmlEnumValue("_belgiumDutch")
    BELGIUM_DUTCH("_belgiumDutch"),
    @XmlEnumValue("_belgiumFrench")
    BELGIUM_FRENCH("_belgiumFrench"),
    @XmlEnumValue("_belizeEnglish")
    BELIZE_ENGLISH("_belizeEnglish"),
    @XmlEnumValue("_bengali")
    BENGALI("_bengali"),
    @XmlEnumValue("_beninFrench")
    BENIN_FRENCH("_beninFrench"),
    @XmlEnumValue("_bermudaEnglish")
    BERMUDA_ENGLISH("_bermudaEnglish"),
    @XmlEnumValue("_bhutanDzongkha")
    BHUTAN_DZONGKHA("_bhutanDzongkha"),
    @XmlEnumValue("_boliviaSpanish")
    BOLIVIA_SPANISH("_boliviaSpanish"),
    @XmlEnumValue("_bonaireSaintEustatiusAndSabaDutch")
    BONAIRE_SAINT_EUSTATIUS_AND_SABA_DUTCH("_bonaireSaintEustatiusAndSabaDutch"),
    @XmlEnumValue("_bosniaAndHerzegovinaBosnian")
    BOSNIA_AND_HERZEGOVINA_BOSNIAN("_bosniaAndHerzegovinaBosnian"),
    @XmlEnumValue("_botswanaEnglish")
    BOTSWANA_ENGLISH("_botswanaEnglish"),
    @XmlEnumValue("_brazilPortuguese")
    BRAZIL_PORTUGUESE("_brazilPortuguese"),
    @XmlEnumValue("_bruneiMalay")
    BRUNEI_MALAY("_bruneiMalay"),
    @XmlEnumValue("_bulgariaBulgarian")
    BULGARIA_BULGARIAN("_bulgariaBulgarian"),
    @XmlEnumValue("_burkinaFasoFrench")
    BURKINA_FASO_FRENCH("_burkinaFasoFrench"),
    @XmlEnumValue("_burundiFrench")
    BURUNDI_FRENCH("_burundiFrench"),
    @XmlEnumValue("_cambodiaKhmer")
    CAMBODIA_KHMER("_cambodiaKhmer"),
    @XmlEnumValue("_cameroonFrench")
    CAMEROON_FRENCH("_cameroonFrench"),
    @XmlEnumValue("_canadaEnglish")
    CANADA_ENGLISH("_canadaEnglish"),
    @XmlEnumValue("_canadaFrench")
    CANADA_FRENCH("_canadaFrench"),
    @XmlEnumValue("_canaryIslandsSpanish")
    CANARY_ISLANDS_SPANISH("_canaryIslandsSpanish"),
    @XmlEnumValue("_capeVerdePortuguese")
    CAPE_VERDE_PORTUGUESE("_capeVerdePortuguese"),
    @XmlEnumValue("_caymanIslandsEnglish")
    CAYMAN_ISLANDS_ENGLISH("_caymanIslandsEnglish"),
    @XmlEnumValue("_centralAfricanRepublicFrench")
    CENTRAL_AFRICAN_REPUBLIC_FRENCH("_centralAfricanRepublicFrench"),
    @XmlEnumValue("_ceutaAndMelillaSpanish")
    CEUTA_AND_MELILLA_SPANISH("_ceutaAndMelillaSpanish"),
    @XmlEnumValue("_chadFrench")
    CHAD_FRENCH("_chadFrench"),
    @XmlEnumValue("_chileSpanish")
    CHILE_SPANISH("_chileSpanish"),
    @XmlEnumValue("_chinaChinese")
    CHINA_CHINESE("_chinaChinese"),
    @XmlEnumValue("_colombiaSpanish")
    COLOMBIA_SPANISH("_colombiaSpanish"),
    @XmlEnumValue("_comorosFrench")
    COMOROS_FRENCH("_comorosFrench"),
    @XmlEnumValue("_congoDemocraticRepublicEnglish")
    CONGO_DEMOCRATIC_REPUBLIC_ENGLISH("_congoDemocraticRepublicEnglish"),
    @XmlEnumValue("_congoDemocraticRepublicFrench")
    CONGO_DEMOCRATIC_REPUBLIC_FRENCH("_congoDemocraticRepublicFrench"),
    @XmlEnumValue("_congoRepublicOfFrench")
    CONGO_REPUBLIC_OF_FRENCH("_congoRepublicOfFrench"),
    @XmlEnumValue("_costaRicaSpanish")
    COSTA_RICA_SPANISH("_costaRicaSpanish"),
    @XmlEnumValue("_coteDivoireFrench")
    COTE_DIVOIRE_FRENCH("_coteDivoireFrench"),
    @XmlEnumValue("_croatiaCroatian")
    CROATIA_CROATIAN("_croatiaCroatian"),
    @XmlEnumValue("_cubaSpanish")
    CUBA_SPANISH("_cubaSpanish"),
    @XmlEnumValue("_curacaoDutch")
    CURACAO_DUTCH("_curacaoDutch"),
    @XmlEnumValue("_cyprusEnglish")
    CYPRUS_ENGLISH("_cyprusEnglish"),
    @XmlEnumValue("_cyprusEnglishEuro")
    CYPRUS_ENGLISH_EURO("_cyprusEnglishEuro"),
    @XmlEnumValue("_czechRepublicCzech")
    CZECH_REPUBLIC_CZECH("_czechRepublicCzech"),
    @XmlEnumValue("_denmarkDanish")
    DENMARK_DANISH("_denmarkDanish"),
    @XmlEnumValue("_djiboutiArabic")
    DJIBOUTI_ARABIC("_djiboutiArabic"),
    @XmlEnumValue("_djiboutiFrench")
    DJIBOUTI_FRENCH("_djiboutiFrench"),
    @XmlEnumValue("_dominicaEnglish")
    DOMINICA_ENGLISH("_dominicaEnglish"),
    @XmlEnumValue("_dominicanRepublicSpanish")
    DOMINICAN_REPUBLIC_SPANISH("_dominicanRepublicSpanish"),
    @XmlEnumValue("_ecuadorSpanish")
    ECUADOR_SPANISH("_ecuadorSpanish"),
    @XmlEnumValue("_egyptArabic")
    EGYPT_ARABIC("_egyptArabic"),
    @XmlEnumValue("_elSalvadorSpanish")
    EL_SALVADOR_SPANISH("_elSalvadorSpanish"),
    @XmlEnumValue("_equatorialGuineaSpanish")
    EQUATORIAL_GUINEA_SPANISH("_equatorialGuineaSpanish"),
    @XmlEnumValue("_eritreaAfar")
    ERITREA_AFAR("_eritreaAfar"),
    @XmlEnumValue("_estoniaEstonian")
    ESTONIA_ESTONIAN("_estoniaEstonian"),
    @XmlEnumValue("_ethiopiaAmharic")
    ETHIOPIA_AMHARIC("_ethiopiaAmharic"),
    @XmlEnumValue("_falklandIslandsEnglish")
    FALKLAND_ISLANDS_ENGLISH("_falklandIslandsEnglish"),
    @XmlEnumValue("_fijiFijian")
    FIJI_FIJIAN("_fijiFijian"),
    @XmlEnumValue("_finlandFinnish")
    FINLAND_FINNISH("_finlandFinnish"),
    @XmlEnumValue("_finlandFinnishEuro")
    FINLAND_FINNISH_EURO("_finlandFinnishEuro"),
    @XmlEnumValue("_franceFrench")
    FRANCE_FRENCH("_franceFrench"),
    @XmlEnumValue("_franceFrenchEuro")
    FRANCE_FRENCH_EURO("_franceFrenchEuro"),
    @XmlEnumValue("_frenchPolynesiaFrench")
    FRENCH_POLYNESIA_FRENCH("_frenchPolynesiaFrench"),
    @XmlEnumValue("_gabonFrench")
    GABON_FRENCH("_gabonFrench"),
    @XmlEnumValue("_gambiaEnglish")
    GAMBIA_ENGLISH("_gambiaEnglish"),
    @XmlEnumValue("_georgiaGeorgian")
    GEORGIA_GEORGIAN("_georgiaGeorgian"),
    @XmlEnumValue("_germanyGerman")
    GERMANY_GERMAN("_germanyGerman"),
    @XmlEnumValue("_germanyGermanEuro")
    GERMANY_GERMAN_EURO("_germanyGermanEuro"),
    @XmlEnumValue("_ghanaEnglish")
    GHANA_ENGLISH("_ghanaEnglish"),
    @XmlEnumValue("_gibraltarEnglish")
    GIBRALTAR_ENGLISH("_gibraltarEnglish"),
    @XmlEnumValue("_goldOunce")
    GOLD_OUNCE("_goldOunce"),
    @XmlEnumValue("_greeceGreek")
    GREECE_GREEK("_greeceGreek"),
    @XmlEnumValue("_grenadaEnglish")
    GRENADA_ENGLISH("_grenadaEnglish"),
    @XmlEnumValue("_guatemalaSpanish")
    GUATEMALA_SPANISH("_guatemalaSpanish"),
    @XmlEnumValue("_guineaBissauPortuguese")
    GUINEA_BISSAU_PORTUGUESE("_guineaBissauPortuguese"),
    @XmlEnumValue("_guineaFrench")
    GUINEA_FRENCH("_guineaFrench"),
    @XmlEnumValue("_guyanaEnglish")
    GUYANA_ENGLISH("_guyanaEnglish"),
    @XmlEnumValue("_haitian")
    HAITIAN("_haitian"),
    @XmlEnumValue("_hondurasSpanish")
    HONDURAS_SPANISH("_hondurasSpanish"),
    @XmlEnumValue("_hongKongChinese")
    HONG_KONG_CHINESE("_hongKongChinese"),
    @XmlEnumValue("_hungaryHungarian")
    HUNGARY_HUNGARIAN("_hungaryHungarian"),
    @XmlEnumValue("_icelandIcelandic")
    ICELAND_ICELANDIC("_icelandIcelandic"),
    @XmlEnumValue("_indiaEnglish")
    INDIA_ENGLISH("_indiaEnglish"),
    @XmlEnumValue("_indiaGujarati")
    INDIA_GUJARATI("_indiaGujarati"),
    @XmlEnumValue("_indiaHindi")
    INDIA_HINDI("_indiaHindi"),
    @XmlEnumValue("_indiaKannada")
    INDIA_KANNADA("_indiaKannada"),
    @XmlEnumValue("_indiaMarathi")
    INDIA_MARATHI("_indiaMarathi"),
    @XmlEnumValue("_indiaPanjabi")
    INDIA_PANJABI("_indiaPanjabi"),
    @XmlEnumValue("_indiaTamil")
    INDIA_TAMIL("_indiaTamil"),
    @XmlEnumValue("_indiaTelugu")
    INDIA_TELUGU("_indiaTelugu"),
    @XmlEnumValue("_indonesiaIndonesian")
    INDONESIA_INDONESIAN("_indonesiaIndonesian"),
    @XmlEnumValue("_iranPersian")
    IRAN_PERSIAN("_iranPersian"),
    @XmlEnumValue("_iraqArabic")
    IRAQ_ARABIC("_iraqArabic"),
    @XmlEnumValue("_irelandEnglish")
    IRELAND_ENGLISH("_irelandEnglish"),
    @XmlEnumValue("_israelHebrew")
    ISRAEL_HEBREW("_israelHebrew"),
    @XmlEnumValue("_italyItalian")
    ITALY_ITALIAN("_italyItalian"),
    @XmlEnumValue("_italyItalianEuro")
    ITALY_ITALIAN_EURO("_italyItalianEuro"),
    @XmlEnumValue("_jamaicaEnglish")
    JAMAICA_ENGLISH("_jamaicaEnglish"),
    @XmlEnumValue("_japanJapanese")
    JAPAN_JAPANESE("_japanJapanese"),
    @XmlEnumValue("_jordanArabic")
    JORDAN_ARABIC("_jordanArabic"),
    @XmlEnumValue("_jordanEnglish")
    JORDAN_ENGLISH("_jordanEnglish"),
    @XmlEnumValue("_kazakhstanRussian")
    KAZAKHSTAN_RUSSIAN("_kazakhstanRussian"),
    @XmlEnumValue("_kenyaEnglish")
    KENYA_ENGLISH("_kenyaEnglish"),
    @XmlEnumValue("_kuwaitArabic")
    KUWAIT_ARABIC("_kuwaitArabic"),
    @XmlEnumValue("_kuwaitEnglish")
    KUWAIT_ENGLISH("_kuwaitEnglish"),
    @XmlEnumValue("_kyrgyzstanRussian")
    KYRGYZSTAN_RUSSIAN("_kyrgyzstanRussian"),
    @XmlEnumValue("_laosLao")
    LAOS_LAO("_laosLao"),
    @XmlEnumValue("_latviaLatvianLettish")
    LATVIA_LATVIAN_LETTISH("_latviaLatvianLettish"),
    @XmlEnumValue("_lebanonArabic")
    LEBANON_ARABIC("_lebanonArabic"),
    @XmlEnumValue("_lesothoEnglish")
    LESOTHO_ENGLISH("_lesothoEnglish"),
    @XmlEnumValue("_liberiaEnglish")
    LIBERIA_ENGLISH("_liberiaEnglish"),
    @XmlEnumValue("_libyaArabic")
    LIBYA_ARABIC("_libyaArabic"),
    @XmlEnumValue("_lithuaniaLithuanian")
    LITHUANIA_LITHUANIAN("_lithuaniaLithuanian"),
    @XmlEnumValue("_luxembourgFrench")
    LUXEMBOURG_FRENCH("_luxembourgFrench"),
    @XmlEnumValue("_luxembourgGerman")
    LUXEMBOURG_GERMAN("_luxembourgGerman"),
    @XmlEnumValue("_luxembourgLuxembourgish")
    LUXEMBOURG_LUXEMBOURGISH("_luxembourgLuxembourgish"),
    @XmlEnumValue("_macauChinese")
    MACAU_CHINESE("_macauChinese"),
    @XmlEnumValue("_macedoniaMacedonian")
    MACEDONIA_MACEDONIAN("_macedoniaMacedonian"),
    @XmlEnumValue("_malawiEnglish")
    MALAWI_ENGLISH("_malawiEnglish"),
    @XmlEnumValue("_malaysiaMalay")
    MALAYSIA_MALAY("_malaysiaMalay"),
    @XmlEnumValue("_maldivesDhivehi")
    MALDIVES_DHIVEHI("_maldivesDhivehi"),
    @XmlEnumValue("_maliFrench")
    MALI_FRENCH("_maliFrench"),
    @XmlEnumValue("_mauritiusEnglish")
    MAURITIUS_ENGLISH("_mauritiusEnglish"),
    @XmlEnumValue("_mexicoSpanish")
    MEXICO_SPANISH("_mexicoSpanish"),
    @XmlEnumValue("_moldovaRomanian")
    MOLDOVA_ROMANIAN("_moldovaRomanian"),
    @XmlEnumValue("_moldovaRussian")
    MOLDOVA_RUSSIAN("_moldovaRussian"),
    @XmlEnumValue("_mongoliaMongolian")
    MONGOLIA_MONGOLIAN("_mongoliaMongolian"),
    @XmlEnumValue("_moroccoArabic")
    MOROCCO_ARABIC("_moroccoArabic"),
    @XmlEnumValue("_mozambiquePortuguese")
    MOZAMBIQUE_PORTUGUESE("_mozambiquePortuguese"),
    @XmlEnumValue("_myanmarBurmese")
    MYANMAR_BURMESE("_myanmarBurmese"),
    @XmlEnumValue("_namibiaEnglish")
    NAMIBIA_ENGLISH("_namibiaEnglish"),
    @XmlEnumValue("_nepalNepali")
    NEPAL_NEPALI("_nepalNepali"),
    @XmlEnumValue("_netherlandsAntillesDutch")
    NETHERLANDS_ANTILLES_DUTCH("_netherlandsAntillesDutch"),
    @XmlEnumValue("_netherlandsDutch")
    NETHERLANDS_DUTCH("_netherlandsDutch"),
    @XmlEnumValue("_netherlandsDutchEuro")
    NETHERLANDS_DUTCH_EURO("_netherlandsDutchEuro"),
    @XmlEnumValue("_newCaledoniaFrench")
    NEW_CALEDONIA_FRENCH("_newCaledoniaFrench"),
    @XmlEnumValue("_newZealandEnglish")
    NEW_ZEALAND_ENGLISH("_newZealandEnglish"),
    @XmlEnumValue("_nicaraguaSpanish")
    NICARAGUA_SPANISH("_nicaraguaSpanish"),
    @XmlEnumValue("_nigerFrench")
    NIGER_FRENCH("_nigerFrench"),
    @XmlEnumValue("_nigeriaEnglish")
    NIGERIA_ENGLISH("_nigeriaEnglish"),
    @XmlEnumValue("_northKoreaKorean")
    NORTH_KOREA_KOREAN("_northKoreaKorean"),
    @XmlEnumValue("_norwayNorwegian")
    NORWAY_NORWEGIAN("_norwayNorwegian"),
    @XmlEnumValue("_omanArabic")
    OMAN_ARABIC("_omanArabic"),
    @XmlEnumValue("_pakistanUrdu")
    PAKISTAN_URDU("_pakistanUrdu"),
    @XmlEnumValue("_palladiumOunce")
    PALLADIUM_OUNCE("_palladiumOunce"),
    @XmlEnumValue("_panamaSpanish")
    PANAMA_SPANISH("_panamaSpanish"),
    @XmlEnumValue("_papuaNewGuineaEnglish")
    PAPUA_NEW_GUINEA_ENGLISH("_papuaNewGuineaEnglish"),
    @XmlEnumValue("_paraguaySpanish")
    PARAGUAY_SPANISH("_paraguaySpanish"),
    @XmlEnumValue("_peruSpanish")
    PERU_SPANISH("_peruSpanish"),
    @XmlEnumValue("_philippinesEnglish")
    PHILIPPINES_ENGLISH("_philippinesEnglish"),
    @XmlEnumValue("_philippinesTagalog")
    PHILIPPINES_TAGALOG("_philippinesTagalog"),
    @XmlEnumValue("_platinumOunce")
    PLATINUM_OUNCE("_platinumOunce"),
    @XmlEnumValue("_polandPolish")
    POLAND_POLISH("_polandPolish"),
    @XmlEnumValue("_portugalPortuguese")
    PORTUGAL_PORTUGUESE("_portugalPortuguese"),
    @XmlEnumValue("_portugalPortugueseEuro")
    PORTUGAL_PORTUGUESE_EURO("_portugalPortugueseEuro"),
    @XmlEnumValue("_puertoRicoSpanish")
    PUERTO_RICO_SPANISH("_puertoRicoSpanish"),
    @XmlEnumValue("_qatarArabic")
    QATAR_ARABIC("_qatarArabic"),
    @XmlEnumValue("_qatarEnglish")
    QATAR_ENGLISH("_qatarEnglish"),
    @XmlEnumValue("_romaniaRomanian")
    ROMANIA_ROMANIAN("_romaniaRomanian"),
    @XmlEnumValue("_russiaRussian")
    RUSSIA_RUSSIAN("_russiaRussian"),
    @XmlEnumValue("_rwandaFrench")
    RWANDA_FRENCH("_rwandaFrench"),
    @XmlEnumValue("_saintBarthelemyFrench")
    SAINT_BARTHELEMY_FRENCH("_saintBarthelemyFrench"),
    @XmlEnumValue("_saintHelenaEnglish")
    SAINT_HELENA_ENGLISH("_saintHelenaEnglish"),
    @XmlEnumValue("_saintKittsAndNevisEnglish")
    SAINT_KITTS_AND_NEVIS_ENGLISH("_saintKittsAndNevisEnglish"),
    @XmlEnumValue("_saintLuciaEnglish")
    SAINT_LUCIA_ENGLISH("_saintLuciaEnglish"),
    @XmlEnumValue("_saintMartinEnglish")
    SAINT_MARTIN_ENGLISH("_saintMartinEnglish"),
    @XmlEnumValue("_saintVincentAndTheGrenadinesEnglish")
    SAINT_VINCENT_AND_THE_GRENADINES_ENGLISH("_saintVincentAndTheGrenadinesEnglish"),
    @XmlEnumValue("_samoaSamoan")
    SAMOA_SAMOAN("_samoaSamoan"),
    @XmlEnumValue("_saoTomeAndPrincipePortuguese")
    SAO_TOME_AND_PRINCIPE_PORTUGUESE("_saoTomeAndPrincipePortuguese"),
    @XmlEnumValue("_saudiArabiaArabic")
    SAUDI_ARABIA_ARABIC("_saudiArabiaArabic"),
    @XmlEnumValue("_senegalFrench")
    SENEGAL_FRENCH("_senegalFrench"),
    @XmlEnumValue("_serbiaAndMontenegroSerbian")
    SERBIA_AND_MONTENEGRO_SERBIAN("_serbiaAndMontenegroSerbian"),
    @XmlEnumValue("_serbiaSerbian")
    SERBIA_SERBIAN("_serbiaSerbian"),
    @XmlEnumValue("_serbiaSerboCroatian")
    SERBIA_SERBO_CROATIAN("_serbiaSerboCroatian"),
    @XmlEnumValue("_seychellesEnglish")
    SEYCHELLES_ENGLISH("_seychellesEnglish"),
    @XmlEnumValue("_seychellesFrench")
    SEYCHELLES_FRENCH("_seychellesFrench"),
    @XmlEnumValue("_sierraLeoneEnglish")
    SIERRA_LEONE_ENGLISH("_sierraLeoneEnglish"),
    @XmlEnumValue("_silverOunce")
    SILVER_OUNCE("_silverOunce"),
    @XmlEnumValue("_singaporeEnglish")
    SINGAPORE_ENGLISH("_singaporeEnglish"),
    @XmlEnumValue("_sintMaartenDutch")
    SINT_MAARTEN_DUTCH("_sintMaartenDutch"),
    @XmlEnumValue("_slovakiaSlovak")
    SLOVAKIA_SLOVAK("_slovakiaSlovak"),
    @XmlEnumValue("_slovakiaSlovakEuro")
    SLOVAKIA_SLOVAK_EURO("_slovakiaSlovakEuro"),
    @XmlEnumValue("_sloveniaSlovenian")
    SLOVENIA_SLOVENIAN("_sloveniaSlovenian"),
    @XmlEnumValue("_sloveniaSlovenianEuro")
    SLOVENIA_SLOVENIAN_EURO("_sloveniaSlovenianEuro"),
    @XmlEnumValue("_solomonIslandsEnglish")
    SOLOMON_ISLANDS_ENGLISH("_solomonIslandsEnglish"),
    @XmlEnumValue("_somaliaSomali")
    SOMALIA_SOMALI("_somaliaSomali"),
    @XmlEnumValue("_southAfricaAfrikaans")
    SOUTH_AFRICA_AFRIKAANS("_southAfricaAfrikaans"),
    @XmlEnumValue("_southAfricaEnglish")
    SOUTH_AFRICA_ENGLISH("_southAfricaEnglish"),
    @XmlEnumValue("_southKoreaKorean")
    SOUTH_KOREA_KOREAN("_southKoreaKorean"),
    @XmlEnumValue("_southSudanEnglish")
    SOUTH_SUDAN_ENGLISH("_southSudanEnglish"),
    @XmlEnumValue("_spainCatalan")
    SPAIN_CATALAN("_spainCatalan"),
    @XmlEnumValue("_spainSpanish")
    SPAIN_SPANISH("_spainSpanish"),
    @XmlEnumValue("_spainSpanishEuro")
    SPAIN_SPANISH_EURO("_spainSpanishEuro"),
    @XmlEnumValue("_sriLankaSinhalese")
    SRI_LANKA_SINHALESE("_sriLankaSinhalese"),
    @XmlEnumValue("_sudanArabic")
    SUDAN_ARABIC("_sudanArabic"),
    @XmlEnumValue("_surinameDutch")
    SURINAME_DUTCH("_surinameDutch"),
    @XmlEnumValue("_swazilandSwati")
    SWAZILAND_SWATI("_swazilandSwati"),
    @XmlEnumValue("_swedenSwedish")
    SWEDEN_SWEDISH("_swedenSwedish"),
    @XmlEnumValue("_switzerlandFrench")
    SWITZERLAND_FRENCH("_switzerlandFrench"),
    @XmlEnumValue("_switzerlandGerman")
    SWITZERLAND_GERMAN("_switzerlandGerman"),
    @XmlEnumValue("_switzerlandItalian")
    SWITZERLAND_ITALIAN("_switzerlandItalian"),
    @XmlEnumValue("_syriaArabic")
    SYRIA_ARABIC("_syriaArabic"),
    @XmlEnumValue("_taiwanChinese")
    TAIWAN_CHINESE("_taiwanChinese"),
    @XmlEnumValue("_tajikistanTajik")
    TAJIKISTAN_TAJIK("_tajikistanTajik"),
    @XmlEnumValue("_tanzaniaEnglish")
    TANZANIA_ENGLISH("_tanzaniaEnglish"),
    @XmlEnumValue("_thailandThai")
    THAILAND_THAI("_thailandThai"),
    @XmlEnumValue("_togoFrench")
    TOGO_FRENCH("_togoFrench"),
    @XmlEnumValue("_tongaTonga")
    TONGA_TONGA("_tongaTonga"),
    @XmlEnumValue("_trinidadAndTobagoEnglish")
    TRINIDAD_AND_TOBAGO_ENGLISH("_trinidadAndTobagoEnglish"),
    @XmlEnumValue("_tunisiaArabic")
    TUNISIA_ARABIC("_tunisiaArabic"),
    @XmlEnumValue("_turkeyTurkish")
    TURKEY_TURKISH("_turkeyTurkish"),
    @XmlEnumValue("_turkmenistanTurkmen")
    TURKMENISTAN_TURKMEN("_turkmenistanTurkmen"),
    @XmlEnumValue("_turksAndCaicosIslandsEnglish")
    TURKS_AND_CAICOS_ISLANDS_ENGLISH("_turksAndCaicosIslandsEnglish"),
    @XmlEnumValue("_ugandaEnglish")
    UGANDA_ENGLISH("_ugandaEnglish"),
    @XmlEnumValue("_ukraineUkrainian")
    UKRAINE_UKRAINIAN("_ukraineUkrainian"),
    @XmlEnumValue("_unitedArabEmiratesArabic")
    UNITED_ARAB_EMIRATES_ARABIC("_unitedArabEmiratesArabic"),
    @XmlEnumValue("_unitedArabEmiratesEnglish")
    UNITED_ARAB_EMIRATES_ENGLISH("_unitedArabEmiratesEnglish"),
    @XmlEnumValue("_unitedKingdomEnglish")
    UNITED_KINGDOM_ENGLISH("_unitedKingdomEnglish"),
    @XmlEnumValue("_unitedStatesEnglish")
    UNITED_STATES_ENGLISH("_unitedStatesEnglish"),
    @XmlEnumValue("_uruguaySpanish")
    URUGUAY_SPANISH("_uruguaySpanish"),
    @XmlEnumValue("_uzbekistanUzbek")
    UZBEKISTAN_UZBEK("_uzbekistanUzbek"),
    @XmlEnumValue("_vanuatuEnglish")
    VANUATU_ENGLISH("_vanuatuEnglish"),
    @XmlEnumValue("_vanuatuFrench")
    VANUATU_FRENCH("_vanuatuFrench"),
    @XmlEnumValue("_venezuelaSpanish")
    VENEZUELA_SPANISH("_venezuelaSpanish"),
    @XmlEnumValue("_vietnamVietnamese")
    VIETNAM_VIETNAMESE("_vietnamVietnamese"),
    @XmlEnumValue("_wallisAndFutunaFrench")
    WALLIS_AND_FUTUNA_FRENCH("_wallisAndFutunaFrench"),
    @XmlEnumValue("_yemenArabic")
    YEMEN_ARABIC("_yemenArabic"),
    @XmlEnumValue("_zambiaEnglish")
    ZAMBIA_ENGLISH("_zambiaEnglish");
    private final String value;

    CurrencyLocale(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CurrencyLocale fromValue(String v) {
        for (CurrencyLocale c: CurrencyLocale.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
