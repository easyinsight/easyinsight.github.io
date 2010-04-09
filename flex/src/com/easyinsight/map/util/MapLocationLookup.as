package com.easyinsight.map.util
{
import mx.collections.ArrayCollection;

public class MapLocationLookup
	{
		private static var usAliasMap:Object = new Object();
		private static var worldAliasMap:Object = new Object();
		
		public function MapLocationLookup()
		{
			usAliasMap["ALABAMA"] = "AL";
			usAliasMap["ALASKA"] = "AK";
			usAliasMap["ARKANSAS"] = "AR";			
			usAliasMap["ARIZONA"] = "AZ";
			usAliasMap["CALIFORNIA"] = "CA";
			usAliasMap["COLORADO"] = "CO";
			usAliasMap["CONNECTICUT"] = "CT";
			usAliasMap["DELAWARE"] = "DE";
			usAliasMap["DISTRICT OF COLUMBIA"] = "DC";
			usAliasMap["FLORIDA"] = "FL";
			usAliasMap["GEORGIA"] = "GA";
			usAliasMap["HAWAII"] = "HI";
			usAliasMap["IDAHO"] = "ID";
			usAliasMap["ILLINOIS"] = "IL";
			usAliasMap["INDIANA"] = "IN";
			usAliasMap["IOWA"] = "IA";
			usAliasMap["KANSAS"] = "KS";
			usAliasMap["KENTUCKY"] = "KY";
			usAliasMap["LOUISIANA"] = "LA";
			usAliasMap["MAINE"] = "ME";
			usAliasMap["MARYLAND"] = "MD";
			usAliasMap["MASSACHUSETTS"] = "MA";
                        
			usAliasMap["MICHIGAN"] = "MI";
			usAliasMap["MINNESOTA"] = "MN";
			usAliasMap["MISSISSIPPI"] = "MS";
			usAliasMap["MISSOURI"] = "MO";
			usAliasMap["MONTANA"] = "MT";
			usAliasMap["NEBRASKA"] = "NE";
			usAliasMap["NEVADA"] = "NV";
			usAliasMap["NEW HAMPSHIRE"] = "NH";
			usAliasMap["NEW JERSEY"] = "NJ";
			usAliasMap["NEW MEXICO"] = "NM";
			usAliasMap["NEW YORK"] = "NY";
			usAliasMap["NORTH CAROLINA"] = "NC";
			usAliasMap["NORTH DAKOTA"] = "ND";
			usAliasMap["OHIO"] = "OH";
			usAliasMap["OKLAHOMA"] = "OK";
			usAliasMap["OREGON"] = "OR";
			usAliasMap["PENNSYLVANIA"] = "PA";
			usAliasMap["RHODE ISLAND"] = "RI";
			usAliasMap["SOUTH CAROLINA"] = "SC";
			usAliasMap["SOUTH DAKOTA"] = "SD";
			usAliasMap["TENNESSEE"] = "TN";
			usAliasMap["TEXAS"] = "TX";
			usAliasMap["UTAH"] = "UT";
			usAliasMap["VERMONT"] = "VT";
			usAliasMap["VIRGINIA"] = "VA";
			usAliasMap["WASHINGTON"] = "WA";
			usAliasMap["WEST VIRGINIA"] = "WV";
			usAliasMap["WISCONSIN"] = "WI";
			usAliasMap["WYOMING"] = "WY";

            worldAliasMap["ARUBA"] = "ABW";
			worldAliasMap["ALBANIA"] = "ALB";
			worldAliasMap["AFGHANISTAN"] = "AFG";
			worldAliasMap["ANGOLA"] = "AGO";
			worldAliasMap["ANGUILLA"] = "AIA";
			worldAliasMap["ANDORRA"] = "AND";
			worldAliasMap["UNITED ARAB EMIRATES"] = "ARE";
			worldAliasMap["ARGENTINA"] = "ARG";
			worldAliasMap["ARMENIA"] = "ARM";
			worldAliasMap["AMERICAN SAMOA"] = "ASM";
			worldAliasMap["ANTIGUA AND BARBUDA"] = "ATG";
			worldAliasMap["AUSTRALIA"] = "AUS";
			worldAliasMap["AUSTRIA"] = "AUT";
			worldAliasMap["AZERBAIJAN"] = "AZE";
			
			worldAliasMap["BURUNDI"] = "BDI";
			worldAliasMap["BELGIUM"] = "BEL";
			worldAliasMap["BENIN"] = "BEN";
			worldAliasMap["BURKINA FASO"] = "BFA";
			worldAliasMap["BANGLADESH"] = "BGD";
			worldAliasMap["BULGARIA"] = "BGR";
			worldAliasMap["BAHRAIN"] = "BHR";
			worldAliasMap["BAHAMAS"] = "BHS";
			worldAliasMap["BOSNIA AND HERZEGOVINA"] = "BIH";
			worldAliasMap["BELARUS"] = "BLR";
			worldAliasMap["BELIZE"] = "BLZ";
			worldAliasMap["BOLIVIA"] = "BOL";
			worldAliasMap["BRAZIL"] = "BRA";
			worldAliasMap["BARBADOS"] = "BRB";
			worldAliasMap["BRUNEI DARUSSALAM"] = "BRN";
			worldAliasMap["BHUTAN"] = "BTN";
			worldAliasMap["BOTSWANA"] = "BWA";
			worldAliasMap["CENTRAL AFRICAN REPUBLIC"] = "CAF";
			worldAliasMap["CANADA"] = "CAN";
			worldAliasMap["SWITZERLAND"] = "CHE";
			worldAliasMap["CHILE"] = "CHL";
			worldAliasMap["CHINA"] = "CHN";
			worldAliasMap["COTE D'IVOIRE"] = "CIV";
			worldAliasMap["CAMEROON"] = "CMR";
			worldAliasMap["CONGO, DEMOCRATIC REPUBLIC OF THE"] = "COD";
			worldAliasMap["CONGO| DEMOCRATIC REPUBLIC OF THE"] = "ZAR";
			worldAliasMap["CONGO, REPUBLIC OF THE"] = "COG";
			worldAliasMap["CONGO| REPUBLIC OF THE"] = "COG";
			worldAliasMap["COLOMBIA"] = "COL";
			worldAliasMap["COMOROS"] = "COM";
			worldAliasMap["CAPE VERDE"] = "CPV";
			worldAliasMap["COSTA RICA"] = "CRI";
			worldAliasMap["CUBA"] = "CUB";
			worldAliasMap["CAYMAN ISLANDS"] = "CYM";
			worldAliasMap["CYPRUS"] = "CYP";
			worldAliasMap["CZECH REPUBLIC"] = "CZE";
			worldAliasMap["GERMANY"] = "DEU";
			worldAliasMap["DJIBOUTI"] = "DJI";
			worldAliasMap["DOMINICA"] = "DMA";
			worldAliasMap["DENMARK"] = "DNK";
			worldAliasMap["DOMINICAN REPUBLIC"] = "DOM";
			worldAliasMap["ALGERIA"] = "DZA";
			worldAliasMap["ECUADOR"] = "ECU";
			worldAliasMap["EGYPT"] = "EGY";
			worldAliasMap["ERITREA"] = "ERI";
			worldAliasMap["SPAIN"] = "ESP";
			worldAliasMap["ESTONIA"] = "EST";
			worldAliasMap["ETHIOPIA"] = "ETH";
			worldAliasMap["FINLAND"] = "FIN";
			worldAliasMap["FIJI"] = "FJI";
			worldAliasMap["FRANCE"] = "FRA";
			worldAliasMap["GABON"] = "GAB";
			worldAliasMap["UNITED KINGDOM"] = "GBR";
			worldAliasMap["GEORGIA"] = "GEO";
			worldAliasMap["GHANA"] = "GHA";
			worldAliasMap["GUINEA"] = "GIN";
			worldAliasMap["GAMBIA"] = "GMB";
			worldAliasMap["GUINEA-BISSAU"] = "GNB";
			worldAliasMap["EQUATORIAL GUINEA"] = "GNQ";
			worldAliasMap["GREECE"] = "GRC";
			worldAliasMap["GRENADA"] = "GRD";
			worldAliasMap["GREENLAND"] = "GRL";
			worldAliasMap["GUATEMALA"] = "GTM";
			worldAliasMap["FRENCH GUIANA"] = "GUF";
			worldAliasMap["GUYANA"] = "GUY";
			worldAliasMap["HONDURAS"] = "HND";
			worldAliasMap["CROATIA"] = "HRV";
			worldAliasMap["HAITI"] = "HTI";
			worldAliasMap["HUNGARY"] = "HUN";
			worldAliasMap["INDONESIA"] = "IDN";
			worldAliasMap["INDIA"] = "IND";
			worldAliasMap["IRELAND"] = "IRL";
			worldAliasMap["IRAN"] = "IRN";
			worldAliasMap["IRAQ"] = "IRQ";
			worldAliasMap["ICELAND"] = "ISL";
			worldAliasMap["ISRAEL"] = "ISR";
			worldAliasMap["ITALY"] = "ITA";
			worldAliasMap["JAMAICA"] = "JAM";
			worldAliasMap["JORDAN"] = "JOR";
			worldAliasMap["JAPAN"] = "JPN";
			worldAliasMap["KAZAKHSTAN"] = "KAZ";
			worldAliasMap["KENYA"] = "KEN";
			worldAliasMap["KYRGYZSTAN"] = "KGZ";
			worldAliasMap["CAMBODIA"] = "KHM";
			worldAliasMap["KIRIBATI"] = "KIR";
			worldAliasMap["SOUTH KOREA"] = "KOR";
			worldAliasMap["KOREA| SOUTH"] = "KOR";
			worldAliasMap["KOREA, SOUTH"] = "KOR";
			worldAliasMap["KUWAIT"] = "KWT";
			worldAliasMap["LAOS"] = "LAO";
			worldAliasMap["LEBANON"] = "LBN";
			worldAliasMap["LIBERIA"] = "LBR";
			worldAliasMap["LIBYA"] = "LBY";
			worldAliasMap["LIECHTENSTEIN"] = "LIE";
			worldAliasMap["SRI LANKA"] = "LKA";
			worldAliasMap["LESOTHO"] = "LSO";
			worldAliasMap["LITHUANIA"] = "LTU";
			worldAliasMap["LUXEMBOURG"] = "LUX";
			worldAliasMap["LATVIA"] = "LVA";
			worldAliasMap["MOROCCO"] = "MAR";
			worldAliasMap["MONACO"] = "MCO";
			worldAliasMap["MOLDOVA"] = "MDA";
			worldAliasMap["MADAGASCAR"] = "MDG";
			worldAliasMap["MALDIVES"] = "MDV";
			worldAliasMap["MEXICO"] = "MEX";
			worldAliasMap["MACEDONIA"] = "MKD";
			worldAliasMap["MALI"] = "MLI";
			worldAliasMap["MALTA"] = "MLT";
			worldAliasMap["MYANMAR"] = "MMR";
            worldAliasMap["BURMA"] = "MMR";
            worldAliasMap["MONTENEGRO"] = "MNE";
			worldAliasMap["MONGOLIA"] = "MNG";
			worldAliasMap["MOZAMBIQUE"] = "MOZ";
			worldAliasMap["MAURITANIA"] = "MRT";
			worldAliasMap["MAURITIUS"] = "MUS";
			worldAliasMap["MALAWI"] = "MWI";
			worldAliasMap["MALAYSIA"] = "MYS";
			worldAliasMap["NAMIBIA"] = "NAM";
			worldAliasMap["NIGER"] = "NER";
			worldAliasMap["NIGERIA"] = "NGA";
			worldAliasMap["NICARAGUA"] = "NIC";	
			worldAliasMap["NORWAY"] = "NOR";
			worldAliasMap["NETHERLANDS"] = "NLD";
			worldAliasMap["NEPAL"] = "NPL";
			worldAliasMap["NAURU"] = "NRU";
			worldAliasMap["NEW ZEALAND"] = "NZL";
			worldAliasMap["OMAN"] = "OMN";
			worldAliasMap["PAKISTAN"] = "PAK";
			worldAliasMap["PANAMA"] = "PAN";
			worldAliasMap["PERU"] = "PER";
			worldAliasMap["PHILIPPINES"] = "PHL";
			worldAliasMap["PAPUA NEW GUINEA"] = "PNG";
			worldAliasMap["POLAND"] = "POL";
			worldAliasMap["NORTH KOREA"] = "PRK";
			worldAliasMap["KOREA| NORTH"] = "PRK";
			worldAliasMap["KOREA, NORTH"] = "PRK";
			worldAliasMap["PORTUGAL"] = "PRT";
			worldAliasMap["PARAGUAY"] = "PRY";
			worldAliasMap["QATAR"] = "QAT";
			worldAliasMap["ROMANIA"] = "ROM";
			worldAliasMap["RUSSIA"] = "RUS";
			worldAliasMap["RWANDA"] = "RWA";
			worldAliasMap["SAUDI ARABIA"] = "SAU";
			worldAliasMap["SUDAN"] = "SDN";
			worldAliasMap["SENEGAL"] = "SEN";
			worldAliasMap["SINGAPORE"] = "SGP";
			worldAliasMap["SIERRA LEONE"] = "SLE";
			worldAliasMap["EL SALVADOR"] = "SLV";
			worldAliasMap["SOMALIA"] = "SOM";
			worldAliasMap["SERBIA"] = "YUG";
			worldAliasMap["SURINAME"] = "SUR";
			worldAliasMap["SLOVAKIA"] = "SVK";
			worldAliasMap["SLOVENIA"] = "SVN";
			worldAliasMap["SWEDEN"] = "SWE";
			worldAliasMap["SWAZILAND"] = "SWZ";
			worldAliasMap["SEYCHELLES"] = "SYC";
			worldAliasMap["SYRIAN ARAB REPUBLIC"] = "SYR";
			worldAliasMap["SYRIA"] = "SYR";
			worldAliasMap["CHAD"] = "TCD";
			worldAliasMap["TOGO"] = "TGO";
			worldAliasMap["TANZANIA"] = "TZA";
			worldAliasMap["THAILAND"] = "THA";
			worldAliasMap["TAJIKISTAN"] = "TJK";
			worldAliasMap["TURKMENISTAN"] = "TKM";
			worldAliasMap["TONGA"] = "TON";
			worldAliasMap["TUNISIA"] = "TUN";
			worldAliasMap["TURKEY"] = "TUR";
			worldAliasMap["TUVALU"] = "TUV";
			worldAliasMap["TAIWAN"] = "TWN";
			worldAliasMap["UGANDA"] = "UGA";
			worldAliasMap["UKRAINE"] = "UKR";
			worldAliasMap["URUGUAY"] = "URY";
			worldAliasMap["UNITED STATES"] = "USA";
			worldAliasMap["UZBEKISTAN"] = "UZB";
			worldAliasMap["VENEZUELA"] = "VEN";
			worldAliasMap["VIETNAM"] = "VNM";
			worldAliasMap["YEMEN"] = "YEM";
			worldAliasMap["SOUTH AFRICA"] = "ZAF";
			worldAliasMap["ZAMBIA"] = "ZMB";
			worldAliasMap["ZIMBABWE"] = "ZWE";
		}
		
		public function getLocation(name:String, mapType:String):String {
			var aliasMap:Object = getAliasMap(mapType);
			var location:String = aliasMap[name.toUpperCase()];
            if (location == null) {
                return name;
            } else {
                return location;
            }
		}

        
		
		private function getAliasMap(mapType:String):Object {
			var aliasMap:Object;
			switch (mapType) {
				case "1":
					aliasMap = usAliasMap;
					break;
				default:
					aliasMap = worldAliasMap;
					break;				
			}
			return aliasMap;
		}

		public static function instance():MapLocationLookup {
			return new MapLocationLookup();
		}

        public function getAllLocations(param:String):Object {
            return getAliasMap(param);
        }}
}