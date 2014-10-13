package com.easyinsight.datafeeds.sample;

import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;

import java.sql.Connection;
import java.util.Date;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 10/13/14
 * Time: 10:50 AM
 */
public class SampleLeadSource extends ServerDataSourceDefinition {

    public static final String LATITUDE = "Latitude";
    public static final String LONGITUDE = "Longitude";
    public static final String STATE = "State or Province";
    public static final String COUNTRY = "Country";
    public static final String COUNT = "Number of Leads";

    public SampleLeadSource() {
        setFeedName("Leads");
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(LATITUDE, new AnalysisDimension());
        fieldBuilder.addField(LONGITUDE, new AnalysisDimension());
        fieldBuilder.addField(STATE, new AnalysisDimension());
        fieldBuilder.addField(COUNTRY, new AnalysisDimension());
        fieldBuilder.addField(COUNT, new AnalysisMeasure());
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.SAMPLE_LEADS;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = new DataSet();
        StringBuilder sb = new StringBuilder();
        sb.append(GEO_DATA1).append(GEO_DATA2).append(GEO3).append(GEO4);
        String[] geoLines = sb.toString().split("\n");
        for (String geoLine : geoLines) {
            String[] geoTokens = geoLine.split(",");
            IRow row = dataSet.createRow();
            row.addValue(keys.get(LATITUDE), geoTokens[0]);
            row.addValue(keys.get(LONGITUDE), geoTokens[3]);
            row.addValue(keys.get(STATE), geoTokens[2]);
            row.addValue(keys.get(COUNTRY), geoTokens[4]);
            row.addValue(keys.get(COUNT), geoTokens[1]);
        }
        return dataSet;
    }

    public static final String GEO_DATA1 = "Latitude,New Visits,Region,Longitude,Country\n" +
            "41.5023,4,Connecticut,-72.9787,United States\n" +
            "51.4545,5,England,-2.5879,United Kingdom\n" +
            "43.0389,1,Wisconsin,-87.9065,United States\n" +
            "43.1979,1,New Hampshire,-70.8737,United States\n" +
            "38.3396,2,California,-122.7011,United States\n" +
            "36.0957,1,North Carolina,-79.4378,United States\n" +
            "30.5052,1,Texas,-97.8203,United States\n" +
            "37.6889,6,Kansas,-97.3361,United States\n" +
            "37.2440,0,Virginia,-77.4103,United States\n" +
            "43.3700,1,Ontario,-80.9822,Canada\n" +
            "46.2857,1,Washington,-119.2845,United States\n" +
            "30.0131,3,Giza,31.2089,Egypt\n" +
            "50.9141,1,England,-1.4986,United Kingdom\n" +
            "50.3272,2,Usti nad Labem Region,13.5458,Czech Republic\n" +
            "50.1264,2,Hesse,8.9283,Germany\n" +
            "0.0000,1,Rio de Janeiro,0.0000,Brazil\n" +
            "59.1776,1,Ostfold,10.8860,Norway\n" +
            "39.8067,1,Kansas,-101.0421,United States\n" +
            "55.4127,0,England,-1.7063,United Kingdom\n" +
            "35.0465,1,South Carolina,-81.9818,United States\n" +
            "45.4207,0,Oregon,-122.6707,United States\n" +
            "40.5134,1,Pennsylvania,-79.9249,United States\n" +
            "37.3230,1,California,-122.0322,United States\n" +
            "40.8214,1,New Jersey,-74.4356,United States\n" +
            "37.4128,0,Virginia,-77.6850,United States\n" +
            "41.8240,1,Rhode Island,-71.4128,United States\n" +
            "35.4887,1,North Carolina,-82.9887,United States\n" +
            "33.6411,1,California,-117.9187,United States\n" +
            "40.2171,2,New Jersey,-74.7429,United States\n" +
            "33.9164,1,California,-118.3526,United States\n" +
            "45.6067,1,Quebec,-73.7124,Canada\n" +
            "53.1004,1,England,-2.4438,United Kingdom\n" +
            "51.6080,1,England,-1.2448,United Kingdom\n" +
            "41.2392,0,Ohio,-81.3459,United States\n" +
            "0.0000,3,Pernambuco,0.0000,Brazil\n" +
            "60.3341,1,Hordaland,5.3810,Norway\n" +
            "44.1512,1,Vermont,-72.6567,United States\n" +
            "37.4636,0,California,-122.4286,United States\n" +
            "43.5890,3,Ontario,-79.6441,Canada\n" +
            "32.7765,1,South Carolina,-79.9311,United States\n" +
            "41.7735,0,Lazio,12.2397,Italy\n" +
            "47.9253,2,North Dakota,-97.0329,United States\n" +
            "39.0182,1,Maryland,-77.2086,United States\n" +
            "33.8170,0,California,-118.0373,United States\n" +
            "35.5849,0,North Carolina,-80.8101,United States\n" +
            "25.0477,7,Dubai,55.1817,United Arab Emirates\n" +
            "42.6875,1,Michigan,-83.2341,United States\n" +
            "40.4568,1,Community of Madrid,-3.4755,Spain\n" +
            "-1.2921,1,(not set),36.8219,Kenya\n" +
            "40.0999,2,Pennsylvania,-75.1528,United States\n" +
            "20.5888,1,Queretaro,-100.3899,Mexico\n" +
            "46.6021,1,Washington,-120.5059,United States\n" +
            "-23.2549,2,Sao Paulo,-47.2931,Brazil\n" +
            "51.4014,1,England,-1.3231,United Kingdom\n" +
            "36.4719,2,Andalusia,-6.1966,Spain\n" +
            "43.4500,4,Ontario,-80.4833,Canada\n" +
            "39.1653,2,Indiana,-86.5264,United States\n" +
            "34.0029,4,Georgia,-84.1446,United States\n" +
            "10.0167,0,(not set),-84.2167,Costa Rica\n" +
            "53.7960,3,England,-1.7594,United Kingdom\n" +
            "25.7215,3,Florida,-80.2684,United States\n" +
            "48.7596,2,Washington,-122.4882,United States\n" +
            "37.8172,1,Kansas,-96.8623,United States\n" +
            "37.4852,8,California,-122.2364,United States\n" +
            "43.6415,7,Maine,-70.2409,United States\n" +
            "-34.8836,3,(not set),-56.1819,Uruguay\n" +
            "33.1032,0,Texas,-96.6705,United States\n" +
            "-8.6500,1,Bali,115.2167,Indonesia\n" +
            "40.7440,2,New Jersey,-74.0324,United States\n" +
            "43.1065,1,New York,-76.2177,United States\n" +
            "38.9717,1,Kansas,-95.2353,United States\n" +
            "31.7683,0,Jerusalem District,35.2137,Israel\n" +
            "45.4215,10,Ontario,-75.6972,Canada\n" +
            "44.0582,3,Oregon,-121.3153,United States\n" +
            "38.8462,1,Virginia,-77.3064,United States\n" +
            "41.8930,3,Massachusetts,-70.9108,United States\n" +
            "55.6473,1,Capital Region of Denmark,12.4149,Denmark\n" +
            "39.7555,1,Colorado,-105.2211,United States\n" +
            "36.0229,1,Oklahoma,-95.9683,United States\n" +
            "62.2426,2,(not set),25.7474,Finland\n" +
            "10.8231,3,Ho Chi Minh City,106.6297,Vietnam\n" +
            "26.6276,1,Florida,-80.1354,United States\n" +
            "0.0000,0,(not set),0.0000,United Kingdom\n" +
            "48.4647,2,Dnipropetrovsk Oblast,35.0462,Ukraine\n" +
            "42.5279,1,Massachusetts,-70.9287,United States\n" +
            "40.7863,1,New Jersey,-74.3301,United States\n" +
            "33.8648,2,Georgia,-84.4644,United States\n" +
            "26.0765,1,Florida,-80.2521,United States\n" +
            "26.1004,3,Florida,-80.3998,United States\n" +
            "36.6777,1,California,-121.6555,United States\n" +
            "35.6581,1,Tokyo,139.7516,Japan\n" +
            "46.0711,2,Friuli-Venezia Giulia,13.2346,Italy\n" +
            "41.6005,1,Iowa,-93.6091,United States\n" +
            "0.0000,1,Ghana,0.0000,Ghana\n" +
            "63.4210,1,Sor-Trondelag,10.3216,Norway\n" +
            "59.9139,18,Oslo,10.7522,Norway\n" +
            "29.3697,1,(not set),47.9783,Kuwait\n" +
            "33.5387,1,Arizona,-112.1860,United States\n" +
            "41.1408,2,Connecticut,-73.2613,United States\n" +
            "45.8206,1,Lombardy,8.8251,Italy\n" +
            "-27.4710,21,Queensland,153.0235,Australia\n" +
            "47.4499,0,Zurich,8.5312,Switzerland\n" +
            "0.0000,1,Para,0.0000,Brazil\n" +
            "41.5737,1,Pennsylvania,-75.5018,United States\n" +
            "33.5333,1,Grand Casablanca,-7.5833,Morocco\n" +
            "4.5981,11,Bogota,-74.0758,Colombia\n" +
            "34.0522,86,California,-118.2437,United States\n" +
            "48.6718,1,Baden-Wurttemberg,9.3828,Germany\n" +
            "27.7731,0,(not set),-82.6400,United States\n" +
            "36.1540,2,Oklahoma,-95.9928,United States\n" +
            "46.0669,1,Alba County,23.5700,Romania\n" +
            "52.8140,1,England,-1.6371,United Kingdom\n" +
            "47.4979,6,Budapest,19.0402,Hungary\n" +
            "39.9307,1,Pennsylvania,-75.3202,United States\n" +
            "38.6609,3,Missouri,-90.4226,United States\n" +
            "38.2975,0,California,-122.2869,United States\n" +
            "54.5705,1,England,-1.3290,United Kingdom\n" +
            "40.5068,1,New Jersey,-74.2654,United States\n" +
            "38.5805,1,California,-121.5302,United States\n" +
            "49.1659,2,British Columbia,-123.9401,Canada\n" +
            "42.2917,3,Michigan,-85.5872,United States\n" +
            "46.0646,1,Washington,-118.3430,United States\n" +
            "43.1947,0,Wisconsin,-88.7290,United States\n" +
            "32.0603,1,Jiangsu,118.7969,China\n" +
            "35.1495,7,Tennessee,-90.0490,United States\n" +
            "48.7308,2,Ile-de-France,2.2714,France\n" +
            "47.1770,1,Washington,-122.1865,United States\n" +
            "41.8781,56,Illinois,-87.6298,United States\n" +
            "53.4433,0,England,-1.9489,United Kingdom\n" +
            "32.3668,1,Alabama,-86.3000,United States\n" +
            "42.5195,4,Massachusetts,-70.8967,United States\n" +
            "52.6424,4,North Holland,5.0602,Netherlands\n" +
            "41.9668,1,Massachusetts,-71.1870,United States\n" +
            "39.7047,5,Colorado,-105.0814,United States\n" +
            "-29.5333,1,KwaZulu-Natal,31.2167,South Africa\n" +
            "47.2327,1,St. Gallen,8.8565,Switzerland\n" +
            "38.6631,1,Missouri,-90.5771,United States\n" +
            "51.5136,0,North Rhine-Westphalia,7.4653,Germany\n" +
            "29.5652,0,Texas,-98.3364,United States\n" +
            "0.0000,2,Cyprus,0.0000,Cyprus\n" +
            "38.9072,22,District of Columbia,-77.0365,United States\n" +
            "39.9936,3,Colorado,-105.0897,United States\n" +
            "46.6713,1,Trentino-Alto Adige/South Tyrol,11.1525,Italy\n" +
            "58.1333,1,Udmurt Republic,52.6667,Russia\n" +
            "53.1934,1,England,-2.8931,United Kingdom\n" +
            "30.4013,1,Louisiana,-91.0009,United States\n" +
            "26.3683,2,Florida,-80.1289,United States\n" +
            "34.1808,2,California,-118.3090,United States\n" +
            "40.4775,1,Colorado,-104.9014,United States\n" +
            "-32.9507,2,Santa Fe Province,-60.6665,Argentina\n" +
            "35.7345,15,North Carolina,-81.3445,United States\n" +
            "-27.1833,0,Queensland,151.2637,Australia\n" +
            "33.6891,4,South Carolina,-78.8867,United States\n" +
            "40.1164,2,Illinois,-88.2434,United States\n" +
            "-25.7461,5,Gauteng,28.1881,South Africa\n" +
            "51.2724,0,England,0.1909,United Kingdom\n" +
            "-23.5505,29,Sao Paulo,-46.6333,Brazil\n" +
            "53.4563,1,England,-2.7371,United Kingdom\n" +
            "33.4255,9,Arizona,-111.9400,United States\n" +
            "30.2841,0,Florida,-81.3961,United States\n" +
            "33.3062,16,Arizona,-111.8413,United States\n" +
            "42.5847,1,Wisconsin,-87.8212,United States\n" +
            "60.2152,1,(not set),24.7188,Finland\n" +
            "36.1080,2,Nevada,-115.2450,United States\n" +
            "34.7229,0,North Carolina,-76.7260,United States\n" +
            "39.8420,1,New Jersey,-75.1854,United States\n" +
            "35.8423,1,Arkansas,-90.7043,United States\n" +
            "37.4138,0,Virginia,-79.1423,United States\n" +
            "48.9006,1,Ile-de-France,2.2593,France\n" +
            "34.7465,3,Arkansas,-92.2896,United States\n" +
            "38.8404,2,Virginia,-77.4289,United States\n" +
            "0.0000,8,(not set),0.0000,(not set)\n" +
            "40.8568,1,New Jersey,-74.1285,United States\n" +
            "40.2969,2,Utah,-111.6946,United States\n" +
            "50.9967,1,North Rhine-Westphalia,8.1112,Germany\n" +
            "34.2073,1,Georgia,-84.1402,United States\n" +
            "3.0906,1,Selangor,101.5296,Malaysia\n" +
            "41.2524,22,Nebraska,-95.9980,United States\n" +
            "39.1031,64,Ohio,-84.5120,United States\n" +
            "45.1392,1,Arges County,24.6792,Romania\n" +
            "35.8978,1,(not set),14.4894,Malta\n" +
            "18.5204,2,Maharashtra,73.8567,India\n" +
            "0.0000,3,Greece,0.0000,Greece\n" +
            "32.2163,1,South Carolina,-80.7526,United States\n" +
            "33.0462,1,Texas,-96.9942,United States\n" +
            "50.0647,2,Lesser Poland Voivodeship,19.9450,Poland\n" +
            "42.0334,4,Illinois,-88.0834,United States\n" +
            "28.8053,2,Texas,-97.0036,United States\n" +
            "54.1452,0,Isle of Man,-4.4817,United Kingdom\n" +
            "47.8601,1,Washington,-122.2043,United States\n" +
            "27.3364,3,Florida,-82.5306,United States\n" +
            "35.0527,1,North Carolina,-78.8784,United States\n" +
            "33.4487,1,Alabama,-86.7878,United States\n" +
            "40.3457,0,Aragon,-1.1064,Spain\n" +
            "41.7534,1,Indiana,-86.1108,United States\n" +
            "41.4570,3,Connecticut,-72.8231,United States\n" +
            "-28.0173,1,Queensland,153.4257,Australia\n" +
            "53.3926,0,England,-2.5870,United Kingdom\n" +
            "36.2081,1,Tennessee,-86.2911,United States\n" +
            "36.8508,1,Virginia,-76.2859,United States\n" +
            "35.7915,2,North Carolina,-78.7811,United States\n" +
            "0.0000,1,Chumphon,0.0000,Thailand\n" +
            "42.2249,0,Oregon,-121.7817,United States\n" +
            "41.2230,2,Utah,-111.9738,United States\n" +
            "43.5279,1,New Hampshire,-71.4704,United States\n" +
            "3.1390,6,Federal Territory of Kuala Lumpur,101.6869,Malaysia\n" +
            "55.7558,24,Moscow,37.6173,Russia\n" +
            "0.0000,1,Maharashtra,0.0000,India\n" +
            "48.8164,1,Ile-de-France,2.3174,France\n" +
            "41.4993,0,Ohio,-81.6944,United States\n" +
            "34.1397,1,California,-118.0353,United States\n" +
            "44.4759,3,Vermont,-73.2121,United States\n" +
            "54.1452,2,(not set),-4.4817,Isle of Man\n" +
            "41.5582,1,Connecticut,-73.0515,United States\n" +
            "36.4247,1,California,-121.3263,United States\n" +
            "41.6611,2,Iowa,-91.5302,United States\n" +
            "30.4278,0,Souss-Massa-Draa,-9.5981,Morocco\n" +
            "43.3845,0,Maine,-70.5440,United States\n" +
            "40.5931,3,New Jersey,-74.6047,United States\n" +
            "40.2338,2,Utah,-111.6585,United States\n" +
            "37.8216,1,California,-122.0000,United States\n" +
            "29.9035,1,Louisiana,-90.0773,United States\n" +
            "52.5620,1,Brandenburg,13.0770,Germany\n" +
            "50.6292,1,Nord-Pas-de-Calais,3.0573,France\n" +
            "32.7155,2,California,-117.1615,United States\n" +
            "43.8570,1,Maine,-70.1031,United States\n" +
            "37.9101,2,California,-122.0652,United States\n" +
            "22.3039,2,Gujarat,70.8022,India\n" +
            "40.2429,1,Pennsylvania,-75.3366,United States\n" +
            "43.4832,1,Aquitaine,-1.5586,France\n" +
            "-23.6742,1,Sao Paulo,-46.5436,Brazil\n" +
            "52.6309,1,England,1.2974,United Kingdom\n" +
            "42.7370,2,Wisconsin,-87.8844,United States\n" +
            "35.9606,6,Tennessee,-83.9207,United States\n" +
            "42.5482,1,Massachusetts,-71.1724,United States\n" +
            "43.8561,5,Ontario,-79.3370,Canada\n" +
            "43.6637,1,Provence-Alpes-Cote d'Azur,7.1488,France\n" +
            "55.1564,0,Region Syddanmark,8.7684,Denmark\n" +
            "34.7745,1,Oklahoma,-96.6783,United States\n" +
            "43.2628,1,Vermont,-72.5951,United States\n" +
            "52.6921,1,Drenthe,6.1937,Netherlands\n" +
            "38.9897,2,Maryland,-76.9378,United States\n" +
            "52.5200,12,Berlin,13.4050,Germany\n" +
            "42.3370,1,Massachusetts,-71.2092,United States\n" +
            "35.9101,1,North Carolina,-79.0753,United States\n" +
            "44.3114,1,South Dakota,-96.7984,United States\n" +
            "39.4353,1,Ohio,-84.2030,United States\n" +
            "49.3167,1,British Columbia,-123.0667,Canada\n" +
            "41.8850,0,Illinois,-87.7845,United States\n" +
            "42.7875,1,Michigan,-86.1089,United States\n" +
            "40.2364,1,Ohio,-83.3671,United States\n" +
            "41.3159,0,Connecticut,-72.3290,United States\n" +
            "38.8809,1,Virginia,-77.3008,United States\n" +
            "41.0053,15,Istanbul Province,28.9770,Turkey\n" +
            "34.0686,1,California,-117.9390,United States\n" +
            "0.0000,2,Costa Rica,0.0000,Costa Rica\n" +
            "43.6615,1,Maine,-70.2553,United States\n" +
            "38.6582,2,Virginia,-77.2497,United States\n" +
            "53.4885,1,England,-2.2983,United Kingdom\n" +
            "43.3239,1,Wisconsin,-88.1668,United States\n" +
            "51.4416,2,North Brabant,5.4697,Netherlands\n" +
            "51.1545,2,(not set),3.2332,Belgium\n" +
            "30.2822,1,Florida,-82.1221,United States\n" +
            "50.7730,1,North Rhine-Westphalia,7.2833,Germany\n" +
            "36.0626,25,Arkansas,-94.1574,United States\n" +
            "30.6697,1,Florida,-81.4626,United States\n" +
            "38.5451,1,Delaware,-75.0891,United States\n" +
            "30.0208,0,Texas,-93.8457,United States\n" +
            "38.9586,6,Virginia,-77.3570,United States\n" +
            "50.1163,1,British Columbia,-122.9574,Canada\n" +
            "14.5547,6,Metro Manila,121.0244,Philippines\n" +
            "41.3851,13,Catalonia,2.1734,Spain\n" +
            "47.1682,1,Pays de la Loire,-1.4734,France\n" +
            "40.6566,1,Viseu District,-7.9125,Portugal\n" +
            "41.5095,0,Illinois,-90.5787,United States\n" +
            "52.0116,0,South Holland,4.3571,Netherlands\n" +
            "6.5244,7,Lagos,3.3792,Nigeria\n" +
            "28.5383,7,Florida,-81.3792,United States\n" +
            "37.7749,79,California,-122.4194,United States\n" +
            "34.4480,25,California,-119.2429,United States\n" +
            "42.0640,1,Massachusetts,-72.4134,United States\n" +
            "38.9680,1,Valencian Community,-0.1845,Spain\n" +
            "34.1425,6,California,-118.2551,United States\n" +
            "40.4127,1,Valencian Community,0.4243,Spain\n" +
            "30.0802,1,Texas,-94.1266,United States\n" +
            "13.7279,7,Bangkok,100.5241,Thailand\n" +
            "37.3541,5,California,-121.9552,United States\n" +
            "16.6913,1,Maharashtra,74.2449,India\n" +
            "33.8950,1,Meknes-Tafilalet,-5.5547,Morocco\n" +
            "-7.2642,1,East Java,112.7456,Indonesia\n" +
            "45.6000,1,(not set),19.5333,Serbia\n" +
            "53.4084,5,England,-2.9916,United Kingdom\n" +
            "38.3841,2,Valencian Community,-0.7668,Spain\n" +
            "-39.0556,0,Taranaki,174.0752,New Zealand\n" +
            "33.4054,3,Alabama,-86.8114,United States\n" +
            "41.7206,1,Illinois,-87.7017,United States\n" +
            "43.5367,1,Midi-Pyrenees,1.5281,France\n" +
            "0.0000,0,Cork,0.0000,Ireland\n" +
            "51.0453,9,Alberta,-114.0581,Canada\n" +
            "51.5756,1,North Rhine-Westphalia,6.5120,Germany\n" +
            "41.4995,1,Ohio,-81.6954,United States\n" +
            "33.9792,0,California,-118.0328,United States\n" +
            "43.6047,1,Midi-Pyrenees,1.4442,France\n" +
            "42.5006,0,Iowa,-90.6646,United States\n" +
            "0.0000,1,Honduras,0.0000,Honduras\n" +
            "55.9533,6,Scotland,-3.1883,United Kingdom\n" +
            "52.3422,1,Gelderland,5.6367,Netherlands\n" +
            "-10.9141,1,Sergipe,-37.6693,Brazil\n" +
            "0.0000,2,(not set),0.0000,Dominican Republic\n" +
            "46.8033,3,Quebec,-71.2428,Canada\n" +
            "0.0000,2,(not set),0.0000,Curaçao\n" +
            "39.0639,2,Colorado,-108.5506,United States\n" +
            "34.4208,36,California,-119.6982,United States\n" +
            "0.0000,0,North Carolina,0.0000,United States\n" +
            "-13.5319,0,Cusco,-71.9675,Peru\n" +
            "25.9397,1,Florida,-81.7075,United States\n" +
            "24.9157,1,Taipei City,121.6739,Taiwan\n" +
            "40.4898,0,Ohio,-81.4457,United States\n" +
            "41.9889,1,Illinois,-88.6868,United States\n" +
            "39.8643,1,New Jersey,-74.8225,United States\n" +
            "43.0987,0,New York,-77.4419,United States\n" +
            "42.6526,0,New York,-73.7562,United States\n" +
            "33.6603,0,California,-117.9992,United States\n" +
            "28.7031,1,Florida,-81.3384,United States\n" +
            "44.4325,12,Bucharest,26.1039,Romania\n" +
            "34.3917,2,California,-118.5426,United States\n" +
            "45.2101,1,Oregon,-123.1987,United States\n" +
            "51.4105,1,England,-0.8339,United Kingdom\n" +
            "63.1767,1,Jamtland County,14.6361,Sweden\n" +
            "-21.9340,1,Sao Paulo,-50.5196,Brazil\n" +
            "0.0000,3,Jakarta,0.0000,Indonesia\n" +
            "42.3735,1,Michigan,-83.5090,United States\n" +
            "37.2638,1,California,-122.0230,United States\n" +
            "40.0362,1,Pennsylvania,-75.5138,United States\n" +
            "51.5831,1,North Brabant,4.7770,Netherlands\n" +
            "8.4874,1,Kerala,76.9520,India\n" +
            "37.5202,1,California,-122.2758,United States\n" +
            "35.9132,1,North Carolina,-79.0558,United States\n" +
            "-25.4244,2,Parana,-49.2654,Brazil\n" +
            "40.1466,4,Pennsylvania,-74.9952,United States\n" +
            "43.3197,0,Wisconsin,-87.9534,United States\n" +
            "53.2706,2,Galway City,-9.0567,Ireland\n" +
            "38.2776,1,Indiana,-85.7372,United States\n" +
            "4.6000,1,(not set),114.3333,Brunei\n" +
            "51.7457,1,England,-2.2178,United Kingdom\n" +
            "37.6819,1,California,-121.7680,United States\n" +
            "39.3455,1,Ohio,-84.5603,United States\n" +
            "43.9200,1,Ontario,-80.0943,Canada\n" +
            "49.3064,1,Baden-Wurttemberg,8.6428,Germany\n" +
            "53.1396,1,Wales,-4.2739,United Kingdom\n" +
            "-29.8265,1,KwaZulu-Natal,30.9704,South Africa\n" +
            "51.3758,2,England,-2.3599,United Kingdom\n" +
            "0.0000,1,Panama,0.0000,Panama\n" +
            "52.5704,1,England,-1.8240,United Kingdom\n" +
            "48.5943,0,Lower Normandy,-0.6504,France\n" +
            "41.4301,1,Ohio,-81.7512,United States\n" +
            "39.0558,5,Kansas,-95.6890,United States\n" +
            "48.1459,2,(not set),17.1071,Slovakia\n" +
            "35.7332,1,North Carolina,-81.3412,United States\n" +
            "0.0000,2,(not set),0.0000,Singapore\n" +
            "24.9243,1,Florida,-80.6278,United States\n" +
            "14.6133,2,(not set),-90.5353,Guatemala\n";

    private static final String GEO_DATA2 =
            "38.2665,1,Virginia,-77.1808,United States\n" +
            "42.0409,2,New York,-74.1182,United States\n" +
            "42.4895,0,Michigan,-83.1446,United States\n" +
            "10.0159,1,Kerala,76.3419,India\n" +
            "48.8515,1,Ile-de-France,2.4759,France\n" +
            "41.0168,1,New Jersey,-74.2057,United States\n" +
            "43.6833,2,Ontario,-79.7667,Canada\n" +
            "49.4928,1,British Columbia,-117.2948,Canada\n" +
            "40.5865,1,California,-122.3917,United States\n" +
            "33.8847,1,California,-118.4109,United States\n" +
            "42.9826,1,New York,-77.4089,United States\n" +
            "43.6776,0,Vermont,-72.7798,United States\n" +
            "48.4284,1,British Columbia,-123.3656,Canada\n" +
            "51.5363,0,England,-1.9092,United Kingdom\n" +
            "56.8790,1,Kronoberg County,14.8059,Sweden\n" +
            "44.2619,1,Wisconsin,-88.4154,United States\n" +
            "-25.9992,1,Gauteng,28.1263,South Africa\n" +
            "40.8097,6,Nebraska,-96.6753,United States\n" +
            "32.4487,1,Texas,-99.7331,United States\n" +
            "40.6461,2,Utah,-111.4980,United States\n" +
            "42.6064,0,Michigan,-83.1498,United States\n" +
            "44.9537,0,Minnesota,-93.0900,United States\n" +
            "-12.0734,2,Lima,-77.0163,Peru\n" +
            "34.7541,2,North Carolina,-77.4302,United States\n" +
            "49.9935,3,Kharkiv Oblast,36.2304,Ukraine\n" +
            "25.7891,7,Florida,-80.2040,United States\n" +
            "43.0962,1,New York,-79.0377,United States\n" +
            "41.3662,1,Catalonia,2.1165,Spain\n" +
            "41.0534,1,Connecticut,-73.5387,United States\n" +
            "41.9270,1,New York,-73.9974,United States\n" +
            "38.7821,1,Virginia,-77.1464,United States\n" +
            "38.8800,10,Virginia,-77.1068,United States\n" +
            "37.5249,1,Virginia,-77.5578,United States\n" +
            "48.1351,8,Bavaria,11.5820,Germany\n" +
            "38.7907,1,California,-121.2358,United States\n" +
            "36.9741,3,California,-122.0308,United States\n" +
            "52.5177,0,England,-1.9952,United Kingdom\n" +
            "38.4701,1,Missouri,-90.3046,United States\n" +
            "35.1595,1,Gwangju,126.8526,South Korea\n" +
            "32.8546,0,South Carolina,-79.9748,United States\n" +
            "35.9186,2,(not set),14.4900,Malta\n" +
            "38.1942,0,Kentucky,-85.5644,United States\n" +
            "-33.8675,301,New South Wales,151.2070,Australia\n" +
            "0.0000,2,(not set),0.0000,Japan\n" +
            "30.6035,1,Alabama,-87.9036,United States\n" +
            "47.9448,1,Washington,-122.0115,United States\n" +
            "59.9929,1,Akershus,11.0422,Norway\n" +
            "52.3238,1,England,0.2725,United Kingdom\n" +
            "54.1090,1,England,-3.2189,United Kingdom\n" +
            "39.3995,2,Ohio,-84.5613,United States\n" +
            "42.4251,1,Massachusetts,-71.0662,United States\n" +
            "42.3876,1,Massachusetts,-71.0995,United States\n" +
            "54.8974,1,England,-1.5174,United Kingdom\n" +
            "33.4270,3,California,-117.6120,United States\n" +
            "55.9007,1,Scotland,-3.5181,United Kingdom\n" +
            "51.0598,2,England,-1.3101,United Kingdom\n" +
            "39.4718,1,Colorado,-104.8948,United States\n" +
            "42.2012,0,Michigan,-85.5800,United States\n" +
            "0.0000,1,Bahia,0.0000,Brazil\n" +
            "40.2983,3,New Jersey,-74.6186,United States\n" +
            "61.0167,1,Khanty-Mansi Autonomous Okrug,69.0333,Russia\n" +
            "36.3729,3,Arkansas,-94.2088,United States\n" +
            "10.7870,4,Tamil Nadu,79.1378,India\n" +
            "44.7647,2,Minnesota,-93.3591,United States\n" +
            "45.6466,1,Lombardy,9.4054,Italy\n" +
            "30.3322,19,Florida,-81.6556,United States\n" +
            "26.2173,1,Florida,-80.2259,United States\n" +
            "52.2215,1,Overijssel,6.8937,Netherlands\n" +
            "42.4772,1,Illinois,-88.0956,United States\n" +
            "42.0834,2,Massachusetts,-71.0184,United States\n" +
            "33.2148,1,Texas,-97.1331,United States\n" +
            "26.8234,1,Florida,-80.1387,United States\n" +
            "50.8225,6,England,-0.1372,United Kingdom\n" +
            "40.7282,51,New Jersey,-74.0776,United States\n" +
            "42.8615,1,New Hampshire,-71.6254,United States\n" +
            "31.9973,0,Texas,-102.0779,United States\n" +
            "42.8709,1,Michigan,-85.8650,United States\n" +
            "-29.8587,1,KwaZulu-Natal,31.0218,South Africa\n" +
            "59.7081,0,Akershus,10.6715,Norway\n" +
            "52.2053,1,England,0.1218,United Kingdom\n" +
            "37.7799,5,California,-121.9780,United States\n" +
            "53.3421,1,England,-2.7297,United Kingdom\n" +
            "51.6879,3,North Brabant,5.0575,Netherlands\n" +
            "54.3807,0,England,-2.9068,United Kingdom\n" +
            "20.6685,0,Hawaii,-156.4391,United States\n" +
            "52.3114,1,North Holland,4.8701,Netherlands\n" +
            "47.1167,1,(not set),51.8833,Kazakhstan\n" +
            "53.5844,2,England,-2.4286,United Kingdom\n" +
            "43.1344,0,Wisconsin,-90.7054,United States\n" +
            "49.6935,2,Alberta,-112.8418,Canada\n" +
            "40.0150,7,Colorado,-105.2705,United States\n" +
            "51.5085,84,England,-0.1255,United Kingdom\n" +
            "42.4851,1,Massachusetts,-71.4328,United States\n" +
            "48.5667,2,Bavaria,13.4319,Germany\n" +
            "39.4015,1,Maryland,-76.6019,United States\n" +
            "20.2961,1,Odisha,85.8245,India\n" +
            "53.0793,1,Bremen,8.8017,Germany\n" +
            "55.5780,0,Scotland,-2.6728,United Kingdom\n" +
            "40.7145,1,Catalonia,0.4928,Spain\n" +
            "36.1224,0,North Carolina,-78.6861,United States\n" +
            "34.9943,1,South Carolina,-81.2420,United States\n" +
            "37.6879,1,California,-122.4702,United States\n" +
            "52.2292,1,North Holland,5.1669,Netherlands\n" +
            "53.2194,2,Groningen,6.5665,Netherlands\n" +
            "55.8642,2,Scotland,-4.2518,United Kingdom\n" +
            "39.8028,2,Colorado,-105.0875,United States\n" +
            "36.0104,1,Tennessee,-84.2696,United States\n" +
            "27.9281,1,Florida,-82.3789,United States\n" +
            "42.6977,3,Sofia-city,23.3219,Bulgaria\n" +
            "41.3497,1,Connecticut,-72.0791,United States\n" +
            "13.1132,1,(not set),-59.5988,Barbados\n" +
            "52.4862,11,England,-1.8904,United Kingdom\n" +
            "45.0761,1,Minnesota,-93.3327,United States\n" +
            "59.4854,0,Rogaland,5.1656,Norway\n" +
            "33.3764,0,California,-117.2512,United States\n" +
            "28.4832,0,Florida,-82.5370,United States\n" +
            "38.9334,0,Maryland,-76.5494,United States\n" +
            "61.4982,1,(not set),23.7611,Finland\n" +
            "34.7371,0,South Carolina,-82.2543,United States\n" +
            "42.1670,1,New York,-76.8205,United States\n" +
            "34.0515,3,Georgia,-84.0713,United States\n" +
            "10.7201,1,Western Visayas,122.5621,Philippines\n" +
            "37.8313,1,California,-122.2852,United States\n" +
            "24.5800,1,Rajasthan,73.6800,India\n" +
            "28.9005,1,Florida,-81.2637,United States\n" +
            "42.5373,2,Massachusetts,-71.5128,United States\n" +
            "29.9660,2,Louisiana,-90.1531,United States\n" +
            "42.6583,2,Massachusetts,-71.1368,United States\n" +
            "53.5445,2,England,-2.1187,United Kingdom\n" +
            "31.7093,1,Texas,-98.9912,United States\n" +
            "47.8107,0,Washington,-122.3774,United States\n" +
            "38.9784,1,Maryland,-76.4922,United States\n" +
            "32.4925,1,Punjab,74.5310,Pakistan\n" +
            "27.9659,1,Florida,-82.8001,United States\n" +
            "52.0607,1,South Holland,4.4940,Netherlands\n" +
            "59.4391,1,Stockholm County,17.9415,Sweden\n" +
            "50.8024,1,Brussels,4.3407,Belgium\n" +
            "36.0835,1,Ibaraki,140.0766,Japan\n" +
            "51.4315,2,England,-0.5155,United Kingdom\n" +
            "48.9471,1,Baden-Wurttemberg,9.4342,Germany\n" +
            "43.4252,1,Provence-Alpes-Cote d'Azur,6.7684,France\n" +
            "26.1276,1,Florida,-80.2331,United States\n" +
            "0.0000,1,Kosovo,0.0000,Kosovo\n" +
            "33.4942,20,Arizona,-111.9261,United States\n" +
            "23.2494,1,Sinaloa,-106.4111,Mexico\n" +
            "55.4765,0,Region Syddanmark,8.4594,Denmark\n" +
            "-16.9203,1,Queensland,145.7709,Australia\n" +
            "-23.5015,1,Sao Paulo,-47.4526,Brazil\n" +
            "0.0000,2,Tuscany,0.0000,Italy\n" +
            "20.7592,0,Hawaii,-156.4572,United States\n" +
            "39.7661,1,Colorado,-105.0772,United States\n" +
            "37.5072,2,California,-122.2605,United States\n" +
            "43.1789,1,Wisconsin,-88.1173,United States\n" +
            "34.2439,1,California,-116.9114,United States\n" +
            "30.7333,2,Chandigarh,76.7794,India\n" +
            "47.1746,1,Canton of Zug,8.5139,Switzerland\n" +
            "35.4868,1,North Carolina,-80.8601,United States\n" +
            "42.1946,2,Oregon,-122.7095,United States\n" +
            "41.5192,1,Ohio,-81.4579,United States\n" +
            "33.7804,3,Georgia,-84.3360,United States\n" +
            "44.6365,1,Oregon,-123.1059,United States\n" +
            "38.4405,1,California,-122.7144,United States\n" +
            "33.1959,1,California,-117.3795,United States\n" +
            "51.9308,2,South Holland,4.4792,Netherlands\n" +
            "40.2025,1,Pennsylvania,-77.1950,United States\n" +
            "47.3223,1,Washington,-122.3126,United States\n" +
            "38.4626,1,Virginia,-77.3889,United States\n" +
            "29.2858,2,Florida,-81.0559,United States\n" +
            "32.3234,1,Arizona,-110.9951,United States\n" +
            "39.2681,1,Ohio,-84.4133,United States\n" +
            "58.8574,1,Rogaland,5.8438,Norway\n" +
            "42.2808,4,Michigan,-83.7430,United States\n" +
            "40.3564,5,New Jersey,-74.8120,United States\n" +
            "-23.3045,1,Parana,-51.1696,Brazil\n" +
            "50.7753,1,North Rhine-Westphalia,6.0839,Germany\n" +
            "42.6889,1,Michigan,-84.2830,United States\n" +
            "38.8916,1,California,-121.2930,United States\n" +
            "34.0556,4,California,-117.1825,United States\n" +
            "43.0718,3,New Hampshire,-70.7626,United States\n" +
            "9.9281,2,(not set),-84.0907,Costa Rica\n" +
            "32.4364,1,Arizona,-111.2224,United States\n" +
            "40.7934,1,Pennsylvania,-77.8600,United States\n" +
            "40.5106,1,New Jersey,-74.6473,United States\n" +
            "53.3498,12,Dublin City,-6.2603,Ireland\n" +
            "38.5816,3,California,-121.4944,United States\n" +
            "44.4057,1,Liguria,8.9463,Italy\n" +
            "28.1511,0,Florida,-82.4615,United States\n" +
            "37.8044,8,California,-122.2711,United States\n" +
            "36.7473,1,Oklahoma,-95.9808,United States\n" +
            "0.0000,1,Kenya,0.0000,Kenya\n" +
            "18.9460,2,Morelos,-99.2231,Mexico\n" +
            "39.4699,1,Valencian Community,-0.3763,Spain\n" +
            "34.5645,0,Arkansas,-92.5868,United States\n" +
            "60.1699,29,(not set),24.9384,Finland\n" +
            "54.6780,0,Northern Ireland,-5.9630,United Kingdom\n" +
            "37.6547,3,California,-122.4077,United States\n" +
            "34.0007,0,South Carolina,-81.0348,United States\n" +
            "51.3830,1,North Rhine-Westphalia,7.7743,Germany\n" +
            "35.9251,1,Tennessee,-86.8689,United States\n" +
            "59.9343,4,Saint Petersburg,30.3351,Russia\n" +
            "40.5622,1,Utah,-111.9297,United States\n" +
            "42.1970,1,Illinois,-88.0934,United States\n" +
            "31.3271,2,Mississippi,-89.2903,United States\n" +
            "45.4322,1,New Brunswick,-65.9463,Canada\n" +
            "42.3584,28,Massachusetts,-71.0598,United States\n" +
            "45.5646,1,Rhone-Alpes,5.9178,France\n" +
            "10.3157,4,Central Visayas,123.8854,Philippines\n" +
            "38.4204,2,West Virginia,-81.7907,United States\n" +
            "47.4874,0,Washington,-117.5758,United States\n" +
            "50.5979,1,(not set),4.3285,Belgium\n" +
            "52.0917,1,Utrecht,5.1178,Netherlands\n" +
            "43.2630,1,Basque Country,-2.9350,Spain\n" +
            "22.3072,1,Gujarat,73.1812,India\n" +
            "48.8944,1,(not set),18.0408,Slovakia\n" +
            "47.4740,1,Washington,-122.2610,United States\n" +
            "-19.9192,1,Minas Gerais,-43.9386,Brazil\n" +
            "38.5396,1,Delaware,-75.0552,United States\n" +
            "41.3359,1,Connecticut,-71.9059,United States\n" +
            "25.3575,1,Sharjah,55.3908,United Arab Emirates\n" +
            "35.1107,2,New Mexico,-106.6100,United States\n" +
            "41.4721,0,Catalonia,2.0865,Spain\n" +
            "36.1667,9,Tennessee,-86.7833,United States\n" +
            "35.9557,1,North Carolina,-80.0053,United States\n" +
            "49.5040,1,British Columbia,-115.0631,Canada\n" +
            "0.0000,3,Santa Catarina,0.0000,Brazil\n" +
            "37.1367,0,Kentucky,-85.9569,United States\n" +
            "38.6780,1,California,-121.1761,United States\n" +
            "51.8642,0,England,-2.2382,United Kingdom\n" +
            "14.4130,1,Calabarzon,120.9737,Philippines\n" +
            "37.7809,2,Missouri,-90.4218,United States\n" +
            "33.6189,3,California,-117.9289,United States\n" +
            "52.3709,0,England,-1.2650,United Kingdom\n" +
            "35.7454,42,North Carolina,-81.6848,United States\n" +
            "44.8041,1,Minnesota,-93.1669,United States\n" +
            "41.2084,2,New York,-73.8913,United States\n" +
            "45.4439,1,Veneto,11.9844,Italy\n" +
            "45.4462,2,Oregon,-122.6393,United States\n" +
            "43.1339,0,Wisconsin,-88.2220,United States\n" +
            "33.8651,0,Georgia,-84.3366,United States\n" +
            "35.6938,4,Tokyo,139.7036,Japan\n" +
            "45.7640,4,Rhone-Alpes,4.8357,France\n" +
            "25.6104,1,Florida,-80.4295,United States\n" +
            "44.9429,3,Oregon,-123.0351,United States\n" +
            "0.0000,9,England,0.0000,United Kingdom\n" +
            "35.0243,0,South Carolina,-81.0279,United States\n" +
            "48.8775,2,Ile-de-France,2.5902,France\n" +
            "41.7698,1,Illinois,-87.9359,United States\n" +
            "42.1015,1,Massachusetts,-72.5898,United States\n" +
            "52.2852,0,England,-1.5201,United Kingdom\n" +
            "42.9764,0,Wisconsin,-88.1084,United States\n" +
            "44.4949,2,Emilia-Romagna,11.3426,Italy\n" +
            "51.5202,1,England,-0.5096,United Kingdom\n" +
            "49.4774,0,Rhineland-Palatinate,8.4452,Germany\n" +
            "51.9647,5,Gelderland,6.2938,Netherlands\n" +
            "43.4643,1,Ontario,-80.5204,Canada\n" +
            "51.0688,1,England,-1.7945,United Kingdom\n" +
            "20.8893,0,Hawaii,-156.4729,United States\n" +
            "48.8848,1,Ile-de-France,2.2685,France\n" +
            "35.0868,1,Tennessee,-89.8101,United States\n" +
            "40.1529,1,Pennsylvania,-76.6027,United States\n" +
            "50.8503,8,Brussels,4.3517,Belgium\n" +
            "47.6062,45,Washington,-122.3321,United States\n" +
            "33.5975,1,Arizona,-112.2718,United States\n" +
            "51.2194,2,(not set),4.4025,Belgium\n" +
            "52.2297,22,Masovian Voivodeship,21.0122,Poland\n" +
            "59.6099,1,Vastmanland County,16.5448,Sweden\n" +
            "10.2553,1,Bolivar,-75.3451,Colombia\n" +
            "42.4919,3,Michigan,-83.0239,United States\n" +
            "39.4812,1,Maryland,-76.6439,United States\n" +
            "36.8841,8,Antalya Province,30.7056,Turkey\n" +
            "38.7521,3,California,-121.2880,United States\n" +
            "43.8106,1,Maine,-70.4169,United States\n" +
            "51.5074,56,England,-0.1278,United Kingdom\n" +
            "40.9701,1,Castile and Leon,-5.6635,Spain\n" +
            "40.2415,2,Pennsylvania,-75.2838,United States\n" +
            "37.3841,2,California,-122.2352,United States\n" +
            "49.0546,1,British Columbia,-122.3280,Canada\n" +
            "52.1936,1,England,-2.2216,United Kingdom\n" +
            "33.7294,6,Islamabad Capital Territory,73.0931,Pakistan\n" +
            "35.2226,2,Oklahoma,-97.4395,United States\n" +
            "50.2649,1,Silesian Voivodeship,19.0238,Poland\n" +
            "37.5585,0,California,-122.2711,United States\n" +
            "43.3623,1,Galicia,-8.4115,Spain\n" +
            "37.5665,5,(not set),126.9780,South Korea\n" +
            "43.8372,3,Ontario,-79.5083,Canada\n" +
            "49.2612,19,British Columbia,-123.1139,Canada\n" +
            "41.9173,1,Illinois,-87.8956,United States\n" +
            "27.5214,0,Florida,-82.5723,United States\n" +
            "28.4636,3,Canary Islands,-16.2518,Spain\n" +
            "38.6008,1,California,-121.3770,United States\n" +
            "41.4793,1,Connecticut,-73.2134,United States\n" +
            "38.2324,1,California,-122.6367,United States\n" +
            "60.3483,1,(not set),24.3724,Finland\n" +
            "39.0331,0,Kentucky,-84.4519,United States\n" +
            "28.5581,1,Florida,-81.8512,United States\n" +
            "40.1857,1,Pennsylvania,-75.4516,United States\n" +
            "43.6108,1,Languedoc-Roussillon,3.8767,France\n" +
            "45.7772,1,Auvergne,3.0870,France\n" +
            "47.6815,3,Washington,-122.2087,United States\n" +
            "39.9945,4,Ohio,-83.0624,United States\n" +
            "44.8831,1,Maine,-68.6719,United States\n" +
            "41.6303,1,Illinois,-87.8539,United States\n" +
            "40.4769,2,New Jersey,-74.5504,United States\n" +
            "59.3289,3,Stockholm County,18.0649,Sweden\n" +
            "13.6929,1,(not set),-89.2182,El Salvador\n" +
            "43.1233,1,New York,-77.5681,United States\n" +
            "43.2500,6,Ontario,-79.8661,Canada\n" +
            "40.3201,1,Pennsylvania,-75.6102,United States\n" +
            "32.0835,2,Georgia,-81.0998,United States\n" +
            "31.0560,0,Texas,-97.4645,United States\n" +
            "30.2672,30,Texas,-97.7431,United States\n" +
            "40.7340,1,New Jersey,-74.3207,United States\n" +
            "41.7637,1,Connecticut,-72.6851,United States\n" +
            "26.2517,1,Florida,-80.1789,United States\n" +
            "35.7796,27,North Carolina,-78.6382,United States\n" +
            "38.9667,1,Kansas,-94.6169,United States\n" +
            "34.1466,1,California,-118.8074,United States\n" +
            "48.4111,1,Montana,-114.3376,United States\n" +
            "35.6127,1,North Carolina,-77.3664,United States\n" +
            "24.8615,2,Sindh,67.0099,Pakistan\n" +
            "36.7213,2,Andalusia,-4.4213,Spain\n" +
            "37.3891,0,Andalusia,-5.9845,Spain\n" +
            "51.7961,1,England,-0.6559,United Kingdom\n" +
            "41.9541,1,Connecticut,-72.7888,United States\n" +
            "27.5217,1,Florida,-82.5276,United States\n" +
            "47.3686,5,Zurich,8.5392,Switzerland\n" +
            "31.6975,1,Chihuahua,-106.4432,Mexico\n" +
            "42.3709,4,Massachusetts,-71.1828,United States\n" +
            "50.3249,1,Silesian Voivodeship,18.7857,Poland\n" +
            "32.7357,1,Texas,-97.1081,United States\n" +
            "33.2443,1,Alabama,-86.8164,United States\n" +
            "51.4288,1,England,-0.5479,United Kingdom\n" +
            "50.0295,2,Hesse,8.6949,Germany\n" +
            "40.6111,2,Utah,-111.8999,United States\n" +
            "35.5641,0,California,-121.0807,United States\n" +
            "43.1536,1,(not set),21.9283,Serbia\n" +
            "9.0667,1,Federal Capital Territory,7.4833,Nigeria\n" +
            "44.6489,3,Nova Scotia,-63.5753,Canada\n" +
            "45.9090,1,Piedmont,8.5056,Italy\n" +
            "-26.6560,16,Queensland,153.0918,Australia\n" +
            "46.1933,1,Rhone-Alpes,6.2342,France\n" +
            "46.0569,1,(not set),14.5058,Slovenia\n" +
            "33.8840,1,Georgia,-84.5144,United States\n" +
            "45.5845,1,Lombardy,9.2744,Italy\n" +
            "8.4875,3,Kerala,76.9525,India\n" +
            "37.5407,5,Virginia,-77.4361,United States\n" +
            "38.4496,4,Virginia,-78.8689,United States\n" +
            "33.9562,1,Georgia,-83.9880,United States\n" +
            "-6.8000,1,(not set),39.2833,Tanzania\n" +
            "34.1954,1,South Carolina,-79.7626,United States\n" +
            "34.2979,1,Georgia,-83.8241,United States\n" +
            "35.5012,1,North Carolina,-80.9787,United States\n" +
            "29.7602,33,Texas,-95.3694,United States\n" +
            "38.5834,1,Missouri,-90.4068,United States\n" +
            "44.3062,0,New Hampshire,-71.7701,United States\n" +
            "31.2001,1,Alexandria Governorate,29.9187,Egypt\n" +
            "48.7998,1,Ile-de-France,2.2573,France\n" +
            "42.8695,1,Michigan,-85.6448,United States\n" +
            "-8.0476,1,Pernambuco,-34.8770,Brazil\n" +
            "60.2048,8,(not set),24.6521,Finland\n" +
            "40.5170,0,Pennsylvania,-80.2214,United States\n" +
            "53.3610,1,England,-2.7336,United Kingdom\n" +
            "32.9618,1,Texas,-96.8292,United States\n" +
            "0.0000,0,Ica,0.0000,Peru\n" +
            "38.9982,2,Virginia,-77.2883,United States\n" +
            "60.8866,0,Hedmark,11.5672,Norway\n" +
            "35.7327,1,North Carolina,-78.8503,United States\n" +
            "45.3657,0,Oregon,-122.6123,United States\n" +
            "13.0524,22,Tamil Nadu,80.2508,India\n" +
            "0.0000,5,Taipei City,0.0000,Taiwan\n" +
            "40.5695,2,New Jersey,-74.6329,United States\n" +
            "42.2393,0,Illinois,-87.9654,United States\n" +
            "32.9912,2,Texas,-97.1950,United States\n" +
            "25.6866,3,Nuevo Leon,-100.3161,Mexico\n" +
            "29.7947,2,Texas,-98.7320,United States\n" +
            "49.8397,3,Lviv Oblast,24.0297,Ukraine\n" +
            "46.4900,1,Ontario,-81.0100,Canada\n" +
            "54.2766,1,Sligo,-8.4761,Ireland\n" +
            "40.2962,1,New Jersey,-74.0510,United States\n" +
            "34.0754,3,Georgia,-84.2941,United States\n" +
            "18.7877,1,Chiang Mai,98.9931,Thailand\n" +
            "-33.9249,14,Western Cape,18.4241,South Africa\n" +
            "40.5881,0,Pennsylvania,-80.0311,United States\n" +
            "-26.1076,7,Gauteng,28.0567,South Africa\n" +
            "36.0999,3,North Carolina,-80.2442,United States\n" +
            "39.0997,2,Missouri,-94.5786,United States\n" +
            "0.0000,1,(not set),0.0000,Ukraine\n" +
            "38.2919,17,California,-122.4580,United States\n" +
            "39.5807,2,Colorado,-104.8772,United States\n" +
            "26.2123,0,Okinawa,127.6792,Japan\n" +
            "29.2097,0,Texas,-99.7862,United States\n" +
            "3.8667,1,(not set),11.5167,Cameroon\n" +
            "37.8858,1,California,-122.1180,United States\n" +
            "35.7081,1,Tokyo,139.7522,Japan\n" +
            "6.2308,1,Antioquia,-75.5906,Colombia\n" +
            "51.9903,2,Utrecht,5.1030,Netherlands\n" +
            "21.4513,1,Hawaii,-158.0153,United States\n" +
            "42.7342,0,Wisconsin,-90.4785,United States\n" +
            "41.0814,1,Ohio,-81.5190,United States\n" +
            "34.9530,1,California,-120.4357,United States\n" +
            "34.0289,2,Georgia,-84.1986,United States\n" +
            "42.9261,0,Wisconsin,-89.3846,United States\n" +
            "42.3384,1,Michigan,-83.8886,United States\n" +
            "56.1589,1,Skane County,13.7668,Sweden\n" +
            "48.1173,1,Brittany,-1.6778,France\n" +
            "52.9548,3,England,-1.1581,United Kingdom\n" +
            "51.5110,1,North Brabant,5.4936,Netherlands\n" +
            "21.1458,1,Maharashtra,79.0882,India\n" +
            "32.9346,1,Texas,-97.2517,United States\n" +
            "42.9869,4,Ontario,-81.2432,Canada\n" +
            "36.8517,1,Nagano,138.3655,Japan\n" +
            "51.5824,1,England,-0.5575,United Kingdom\n" +
            "39.0438,4,Virginia,-77.4874,United States\n" +
            "47.2184,1,Pays de la Loire,-1.5536,France\n" +
            "33.7701,2,California,-118.1937,United States\n" +
            "40.5725,4,Utah,-111.8597,United States\n" +
            "42.5879,1,Massachusetts,-72.5994,United States\n" +
            "29.5294,1,Texas,-95.2010,United States\n" +
            "3.4206,1,Valle del Cauca,-76.5222,Colombia\n" +
            "18.8053,1,Veracruz,-97.1795,Mexico\n" +
            "47.8209,1,Washington,-122.3151,United States\n" +
            "40.2115,2,New Jersey,-74.6797,United States\n" +
            "37.5934,1,California,-122.0438,United States\n" +
            "33.6391,2,Arizona,-112.3958,United States\n" +
            "34.4890,1,California,-118.6256,United States\n" +
            "40.7128,132,New York,-74.0059,United States\n" +
            "53.2482,1,Wales,-3.1358,United Kingdom\n" +
            "63.0951,1,(not set),21.6164,Finland\n" +
            "39.2497,1,Nevada,-119.9527,United States\n" +
            "51.6286,0,England,-0.7482,United Kingdom\n" +
            "52.2689,1,Lower Saxony,10.5268,Germany\n" +
            "43.2342,1,Michigan,-86.2484,United States\n" +
            "39.5296,3,Nevada,-119.8138,United States\n" +
            "39.7092,0,Ohio,-84.0633,United States\n" +
            "42.6112,0,Massachusetts,-71.5745,United States\n" +
            "42.3314,3,Michigan,-83.0458,United States\n" +
            "12.9716,48,Karnataka,77.5946,India\n" +
            "43.8167,1,Idaho,-111.7833,United States\n" +
            "43.5407,0,Idaho,-116.5635,United States\n" +
            "0.0000,1,Jamaica,0.0000,Jamaica\n" +
            "33.9631,1,California,-117.5639,United States\n" +
            "30.8207,1,Alabama,-88.0706,United States\n" +
            "41.7614,1,Massachusetts,-70.7197,United States\n" +
            "42.2793,1,Massachusetts,-71.4162,United States\n" +
            "42.0334,2,Illinois,-87.8834,United States\n" +
            "35.4495,1,Oklahoma,-97.3967,United States\n" +
            "-30.0347,1,Rio Grande do Sul,-51.2177,Brazil\n" +
            "0.0000,1,Uruguay,0.0000,Uruguay\n" +
            "33.8314,1,California,-118.2820,United States\n" +
            "9.9252,1,Tamil Nadu,78.1198,India\n" +
            "27.2731,1,Florida,-80.3582,United States\n" +
            "35.0456,2,Tennessee,-85.3097,United States\n" +
            "42.2287,1,Massachusetts,-71.5226,United States\n" +
            "47.5674,1,Basel-Stadt,7.5976,Switzerland\n" +
            "39.6671,2,Delaware,-75.7269,United States\n" +
            "34.0333,1,Fes-Boulemane,-5.0000,Morocco\n" +
            "43.1610,1,New York,-77.6109,United States\n" +
            "28.2920,1,Florida,-81.4076,United States\n" +
            "40.1304,2,Pennsylvania,-75.5149,United States\n" +
            "33.7475,1,California,-116.9720,United States\n" +
            "41.9750,1,Illinois,-88.0073,United States\n" +
            "0.0000,1,Rio Grande do Norte,0.0000,Brazil\n" +
            "50.7192,1,England,-1.8808,United Kingdom\n" +
            "39.8628,2,Castile-La Mancha,-4.0273,Spain\n" +
            "42.1944,0,Massachusetts,-71.1990,United States\n" +
            "51.5745,1,England,-0.7801,United Kingdom\n" +
            "39.2037,2,Maryland,-76.8610,United States\n" +
            "-40.3523,1,Manawatu-Wanganui,175.6082,New Zealand\n" +
            "50.1467,1,Hesse,8.5615,Germany\n" +
            "51.3397,2,Saxony,12.3731,Germany\n";

    public static final String GEO3 =
            "40.6084,1,Pennsylvania,-75.4902,United States\n" +
            "51.4556,2,North Rhine-Westphalia,7.0116,Germany\n" +
            "26.0112,2,Florida,-80.1495,United States\n" +
            "34.0195,3,California,-118.4912,United States\n" +
            "37.2636,1,Gyeonggi-do,127.0286,South Korea\n" +
            "27.4467,5,Florida,-80.3256,United States\n" +
            "42.0406,1,Illinois,-87.7826,United States\n" +
            "28.3886,0,Florida,-81.5659,United States\n" +
            "42.6389,0,Michigan,-83.2910,United States\n" +
            "49.9915,3,Hesse,8.6634,Germany\n" +
            "40.8207,2,New Jersey,-74.2938,United States\n" +
            "-29.8167,1,KwaZulu-Natal,30.8500,South Africa\n" +
            "50.0409,1,Podkarpackie Voivodeship,21.9993,Poland\n" +
            "51.8156,1,England,-0.8084,United Kingdom\n" +
            "29.4241,3,Texas,-98.4936,United States\n" +
            "0.0000,0,Troms,0.0000,Norway\n" +
            "28.0197,0,Florida,-82.7718,United States\n" +
            "33.3528,17,Arizona,-111.7890,United States\n" +
            "53.0027,2,England,-2.1794,United Kingdom\n" +
            "42.2409,1,Michigan,-83.2697,United States\n" +
            "42.0451,1,Illinois,-87.6877,United States\n" +
            "37.4419,7,California,-122.1430,United States\n" +
            "37.6485,6,California,-118.9721,United States\n" +
            "37.1305,1,California,-121.6544,United States\n" +
            "-26.1883,1,Gauteng,28.3206,South Africa\n" +
            "40.4260,5,Community of Madrid,-3.5652,Spain\n" +
            "50.7459,1,Hainaut,3.2193,Belgium\n" +
            "37.9060,2,California,-122.5450,United States\n" +
            "43.8363,1,Ontario,-79.8745,Canada\n" +
            "48.2082,7,Vienna,16.3738,Austria\n" +
            "41.2841,11,Connecticut,-73.4975,United States\n" +
            "46.7382,3,Quebec,-71.2465,Canada\n" +
            "39.9523,8,Pennsylvania,-75.1638,United States\n" +
            "51.1079,1,Lower Silesian Voivodeship,17.0385,Poland\n" +
            "28.4863,1,Florida,-81.5542,United States\n" +
            "47.4469,1,Washington,-122.1436,United States\n" +
            "0.0000,1,PR-AT LARGE,0.0000,Puerto Rico\n" +
            "0.0000,2,(not set),0.0000,Lithuania\n" +
            "33.7445,1,California,-118.3870,United States\n" +
            "43.5448,4,Ontario,-80.2482,Canada\n" +
            "36.8529,3,Virginia,-75.9780,United States\n" +
            "34.2257,6,North Carolina,-77.9447,United States\n" +
            "43.0731,34,Wisconsin,-89.4012,United States\n" +
            "40.3273,4,Pennsylvania,-76.0110,United States\n" +
            "51.6008,1,Wales,-3.3423,United Kingdom\n" +
            "56.0717,2,Scotland,-3.4522,United Kingdom\n" +
            "53.2777,1,England,-2.7659,United Kingdom\n" +
            "10.5000,1,Caracas Metropolitan District,-66.9167,Venezuela\n" +
            "48.4278,1,Quebec,-71.0592,Canada\n" +
            "38.0406,3,Kentucky,-84.5037,United States\n" +
            "39.0446,2,Maryland,-77.1189,United States\n" +
            "48.7754,1,Baden-Wurttemberg,9.1818,Germany\n" +
            "37.5779,2,California,-122.3481,United States\n" +
            "41.1650,1,Illinois,-87.8785,United States\n" +
            "52.4068,2,England,-1.5197,United Kingdom\n" +
            "29.6239,1,Florida,-81.8904,United States\n" +
            "43.9269,1,Midi-Pyrenees,2.1463,France\n" +
            "39.9403,1,Ohio,-82.0132,United States\n" +
            "44.9521,1,Crimea,34.1024,Ukraine\n" +
            "39.1014,1,Colorado,-104.8475,United States\n" +
            "42.3440,1,Castile and Leon,-3.6969,Spain\n" +
            "43.2123,1,New York,-77.4300,United States\n" +
            "38.7965,1,Maryland,-76.8836,United States\n" +
            "45.4654,2,Lombardy,9.1859,Italy\n" +
            "-31.3989,1,Cordoba,-64.1821,Argentina\n" +
            "54.6565,2,Northern Ireland,-5.6753,United Kingdom\n" +
            "41.2960,1,Connecticut,-72.3845,United States\n" +
            "7.8942,1,North Santander,-72.5039,Colombia\n" +
            "43.1836,0,Wisconsin,-89.2137,United States\n" +
            "51.6197,0,North Brabant,5.4335,Netherlands\n" +
            "41.8778,0,Illinois,-87.9028,United States\n" +
            "40.4129,37,New Jersey,-74.3073,United States\n" +
            "52.5185,1,Flevoland,5.4714,Netherlands\n" +
            "41.6100,1,Illinois,-87.6467,United States\n" +
            "33.7392,1,Georgia,-84.1658,United States\n" +
            "33.8545,2,Georgia,-84.2171,United States\n" +
            "37.7652,1,California,-122.2416,United States\n" +
            "28.0395,7,Florida,-81.9498,United States\n" +
            "40.3026,96,New Jersey,-74.5112,United States\n" +
            "0.0000,1,Gharbia,0.0000,Egypt\n" +
            "33.7490,29,Georgia,-84.3880,United States\n" +
            "42.3265,0,Oregon,-122.8756,United States\n" +
            "40.2987,1,Ohio,-83.0680,United States\n" +
            "41.3114,3,Wyoming,-105.5911,United States\n" +
            "35.3187,2,North Carolina,-82.4610,United States\n" +
            "39.7391,1,Delaware,-75.5398,United States\n" +
            "33.7456,1,California,-117.8678,United States\n" +
            "41.9243,1,Connecticut,-72.6454,United States\n" +
            "39.9607,4,Pennsylvania,-75.6055,United States\n" +
            "-32.2438,1,New South Wales,148.6095,Australia\n" +
            "52.6369,2,England,-1.1398,United Kingdom\n" +
            "40.7968,1,New Jersey,-74.4815,United States\n" +
            "41.6023,1,Connecticut,-72.9868,United States\n" +
            "42.6584,2,Michigan,-83.1499,United States\n" +
            "41.6639,1,Ohio,-83.5552,United States\n" +
            "37.5483,5,California,-121.9886,United States\n" +
            "33.6534,1,Georgia,-84.4494,United States\n" +
            "44.1858,1,Wisconsin,-88.4626,United States\n" +
            "45.8399,13,Oregon,-119.7006,United States\n" +
            "28.8029,6,Florida,-81.2695,United States\n" +
            "54.8985,0,(not set),23.9036,Lithuania\n" +
            "33.0198,9,Texas,-96.6989,United States\n" +
            "52.3333,1,North Holland,4.7833,Netherlands\n" +
            "49.4520,8,Bavaria,11.0768,Germany\n" +
            "33.5207,3,Alabama,-86.8025,United States\n" +
            "30.0074,6,Cairo Governorate,31.4913,Egypt\n" +
            "39.4195,3,Maryland,-76.7803,United States\n" +
            "34.1015,2,Georgia,-84.5194,United States\n" +
            "46.5619,1,Quebec,-72.7435,Canada\n" +
            "52.9133,4,England,-1.1165,United Kingdom\n" +
            "-0.2232,1,Pichincha,-78.5127,Ecuador\n" +
            "47.5301,1,Washington,-122.0326,United States\n" +
            "31.8457,1,Texas,-102.3676,United States\n" +
            "50.4501,3,Kyiv city,30.5234,Ukraine\n" +
            "3.1071,1,Selangor,101.6083,Malaysia\n" +
            "38.8106,1,Missouri,-90.6998,United States\n" +
            "0.0000,1,Minas Gerais,0.0000,Brazil\n" +
            "46.7667,1,Cluj County,23.5833,Romania\n" +
            "39.8268,1,New Jersey,-75.0154,United States\n" +
            "0.0000,3,(not set),0.0000,Peru\n" +
            "0.0000,1,Galicia,0.0000,Spain\n" +
            "43.3255,2,Ontario,-79.7990,Canada\n" +
            "40.1023,0,Pennsylvania,-75.2743,United States\n" +
            "-23.2988,1,Sao Paulo,-45.9663,Brazil\n" +
            "42.7469,1,New York,-73.7589,United States\n" +
            "43.4930,1,Aquitaine,-1.4748,France\n" +
            "37.3509,1,Andalusia,-6.0520,Spain\n" +
            "33.4251,1,Texas,-94.0477,United States\n" +
            "37.5297,1,California,-122.0402,United States\n" +
            "29.9974,1,Texas,-98.0986,United States\n" +
            "41.7606,22,Illinois,-88.3201,United States\n" +
            "56.1629,10,Central Denmark Region,10.2039,Denmark\n" +
            "49.7516,1,Bavaria,11.5432,Germany\n" +
            "43.6122,2,Midi-Pyrenees,1.3366,France\n" +
            "55.6596,1,Capital Region of Denmark,12.3571,Denmark\n" +
            "41.9001,3,Massachusetts,-71.0898,United States\n" +
            "34.1443,1,California,-118.0019,United States\n" +
            "55.6761,10,Capital Region of Denmark,12.5683,Denmark\n" +
            "51.6978,1,North Brabant,5.3037,Netherlands\n" +
            "51.8915,0,Gelderland,6.3834,Netherlands\n" +
            "42.5494,1,Michigan,-83.6136,United States\n" +
            "60.4843,1,Dalarna County,15.4340,Sweden\n" +
            "35.2828,1,California,-120.6596,United States\n" +
            "50.2851,1,Saskatchewan,-107.7972,Canada\n" +
            "39.4143,2,Maryland,-77.4105,United States\n" +
            "35.7667,0,Tangier-Tetouan,-5.8000,Morocco\n" +
            "57.1497,4,Scotland,-2.0943,United Kingdom\n" +
            "49.2221,1,British Columbia,-122.6188,Canada\n" +
            "45.6387,5,Washington,-122.6615,United States\n" +
            "41.6821,0,Massachusetts,-69.9598,United States\n" +
            "51.2277,6,North Rhine-Westphalia,6.7735,Germany\n" +
            "0.0000,1,Kuwait,0.0000,Kuwait\n" +
            "40.0456,1,Indiana,-86.0086,United States\n" +
            "51.3896,1,England,1.3868,United Kingdom\n" +
            "38.2527,3,Kentucky,-85.7585,United States\n" +
            "25.8576,2,Florida,-80.2781,United States\n" +
            "46.9479,4,Canton of Bern,7.4446,Switzerland\n" +
            "46.9930,1,Canton of Neuchatel,6.9319,Switzerland\n" +
            "49.2838,1,British Columbia,-122.7932,Canada\n" +
            "-19.9245,5,Minas Gerais,-43.9352,Brazil\n" +
            "51.5842,2,Wales,-2.9977,United Kingdom\n" +
            "39.5523,1,Ohio,-84.2333,United States\n" +
            "41.9908,1,Illinois,-87.8739,United States\n" +
            "43.5446,3,South Dakota,-96.7311,United States\n" +
            "38.8814,1,Kansas,-94.8191,United States\n" +
            "52.0823,1,South Holland,4.7461,Netherlands\n" +
            "40.0821,1,New Jersey,-74.2097,United States\n" +
            "44.5008,1,Ontario,-80.2169,Canada\n" +
            "42.0842,1,Illinois,-88.1547,United States\n" +
            "38.6971,1,Lisbon,-9.4223,Portugal\n" +
            "13.6288,1,Andhra Pradesh,79.4192,India\n" +
            "40.3489,1,Colorado,-104.7019,United States\n" +
            "31.2989,1,Jiangsu,120.5853,China\n" +
            "55.1833,1,(not set),30.1667,Belarus\n" +
            "39.9784,1,Indiana,-86.1180,United States\n" +
            "34.9387,1,South Carolina,-82.2271,United States\n" +
            "33.9533,1,California,-117.3962,United States\n" +
            "51.4176,1,North Brabant,5.4060,Netherlands\n" +
            "-2.9909,1,South Sumatra,104.7566,Indonesia\n" +
            "0.0000,0,Saint Peter Port,0.0000,Guernsey\n" +
            "26.2670,2,Florida,-81.7896,United States\n" +
            "56.4620,1,Scotland,-2.9707,United Kingdom\n" +
            "41.1947,1,New Jersey,-74.4938,United States\n" +
            "42.5039,2,Massachusetts,-71.0723,United States\n" +
            "29.6911,3,Texas,-95.2091,United States\n" +
            "51.3762,1,England,-0.0982,United Kingdom\n" +
            "33.4484,117,Arizona,-112.0740,United States\n" +
            "37.3568,0,Virginia,-77.4417,United States\n" +
            "37.6413,0,California,-120.7605,United States\n" +
            "52.3759,0,Lower Saxony,9.7320,Germany\n" +
            "33.3807,1,Georgia,-84.7997,United States\n" +
            "40.6183,1,Utah,-111.8163,United States\n" +
            "26.6406,0,Florida,-81.8723,United States\n" +
            "53.6979,1,England,-2.6955,United Kingdom\n" +
            "41.3145,0,Ohio,-81.8357,United States\n" +
            "40.0992,8,Ohio,-83.1141,United States\n" +
            "0.0000,1,Nova Scotia,0.0000,Canada\n" +
            "51.4268,2,England,-0.3313,United Kingdom\n" +
            "0.0000,1,(not set),0.0000,Ecuador\n" +
            "51.7189,1,North Rhine-Westphalia,8.7575,Germany\n" +
            "41.2028,1,Apulia,16.5987,Italy\n" +
            "49.5614,1,Vysocina Region,16.0742,Czech Republic\n" +
            "37.3861,9,California,-122.0839,United States\n" +
            "51.9217,1,South Holland,4.4811,Netherlands\n" +
            "40.1150,1,Utah,-111.6549,United States\n" +
            "52.2405,0,England,-0.9027,United Kingdom\n" +
            "42.3918,0,Massachusetts,-71.0328,United States\n" +
            "33.5507,1,California,-117.6412,United States\n" +
            "39.9208,3,Ankara Province,32.8541,Turkey\n" +
            "40.7887,1,New York,-73.6474,United States\n" +
            "49.8880,0,British Columbia,-119.4960,Canada\n" +
            "32.2988,1,Mississippi,-90.1848,United States\n" +
            "-16.5000,1,La Paz Department,-68.1500,Bolivia\n" +
            "49.4230,3,Khmel'nyts'ka oblast,26.9871,Ukraine\n" +
            "29.6516,1,Florida,-82.3248,United States\n" +
            "50.8569,0,England,-2.1654,United Kingdom\n" +
            "52.2621,1,England,-2.1479,United Kingdom\n" +
            "41.0037,0,Pennsylvania,-76.4549,United States\n" +
            "32.4285,1,Mississippi,-90.1323,United States\n" +
            "40.8682,2,New York,-73.4257,United States\n" +
            "35.9799,3,North Carolina,-78.5097,United States\n" +
            "36.1408,1,(not set),-5.3536,Gibraltar\n" +
            "43.7508,1,Wisconsin,-87.7145,United States\n" +
            "42.5000,1,South Dakota,-96.4003,United States\n" +
            "34.0209,1,Rabat-Sale-Zemmour-Zaer,-6.8416,Morocco\n" +
            "35.3733,1,California,-119.0187,United States\n" +
            "14.2843,2,Calabarzon,121.0889,Philippines\n" +
            "39.8680,2,Colorado,-104.9719,United States\n" +
            "-37.8141,44,Victoria,144.9633,Australia\n" +
            "49.3349,1,British Columbia,-123.1668,Canada\n" +
            "37.6076,1,Virginia,-77.4769,United States\n" +
            "48.8422,1,Ile-de-France,2.6980,France\n" +
            "41.6886,1,Rhode Island,-71.5642,United States\n" +
            "37.6027,2,California,-120.8665,United States\n" +
            "10.0636,1,Lara,-69.3347,Venezuela\n" +
            "43.4530,1,Michigan,-84.0273,United States\n" +
            "49.2451,2,Rhineland-Palatinate,7.3634,Germany\n" +
            "33.7878,3,California,-117.8531,United States\n" +
            "42.8142,1,New York,-73.9396,United States\n" +
            "41.5768,1,Pennsylvania,-75.2588,United States\n" +
            "49.2295,0,British Columbia,-123.0026,Canada\n" +
            "37.3688,2,California,-122.0364,United States\n" +
            "44.6488,1,Emilia-Romagna,10.9201,Italy\n" +
            "41.3839,1,Connecticut,-72.9026,United States\n" +
            "-33.9581,3,Eastern Cape,25.6000,South Africa\n" +
            "39.9778,5,Colorado,-105.1319,United States\n" +
            "32.9628,1,California,-117.0359,United States\n" +
            "29.0730,1,Sonora,-110.9559,Mexico\n" +
            "-33.7150,1,New South Wales,150.3114,Australia\n" +
            "50.8351,1,England,-0.1720,United Kingdom\n" +
            "-38.1368,1,Bay Of Plenty,176.2497,New Zealand\n" +
            "40.4658,2,Pennsylvania,-80.1317,United States\n" +
            "18.4663,1,(not set),-66.1057,Puerto Rico\n" +
            "42.5869,1,Michigan,-82.9196,United States\n" +
            "30.4383,6,Florida,-84.2807,United States\n" +
            "36.1156,1,Oklahoma,-97.0584,United States\n" +
            "-27.5949,2,Santa Catarina,-48.5482,Brazil\n" +
            "45.5914,1,Quebec,-73.4364,Canada\n" +
            "40.3978,1,Colorado,-105.0750,United States\n" +
            "59.7352,0,Akershus,10.9082,Norway\n" +
            "3.0993,1,Selangor,101.6449,Malaysia\n" +
            "42.4048,1,Ontario,-82.1910,Canada\n" +
            "41.4820,0,Ohio,-81.7982,United States\n" +
            "50.0755,5,Prague,14.4378,Czech Republic\n" +
            "39.5349,2,Nevada,-119.7527,United States\n" +
            "20.8569,0,Hawaii,-156.3130,United States\n" +
            "38.9085,1,Balearic Islands,1.4266,Spain\n" +
            "0.0000,1,Saarland,0.0000,Germany\n" +
            "42.2626,1,Massachusetts,-71.8023,United States\n" +
            "37.1299,1,Virginia,-80.4089,United States\n" +
            "39.8007,0,Delaware,-75.4596,United States\n" +
            "37.5630,8,California,-122.3255,United States\n" +
            "41.5758,1,Connecticut,-72.5026,United States\n" +
            "-34.6037,8,Autonomous City of Buenos Aires,-58.3816,Argentina\n" +
            "52.7664,1,England,-0.8871,United Kingdom\n" +
            "51.3404,2,England,0.7316,United Kingdom\n" +
            "0.0000,2,Albania,0.0000,Albania\n" +
            "35.3460,1,North Carolina,-79.4170,United States\n" +
            "25.7907,1,Florida,-80.1301,United States\n" +
            "41.7759,1,Connecticut,-72.5215,United States\n" +
            "39.9568,2,Indiana,-86.0134,United States\n" +
            "63.8259,1,Vasterbotten County,20.2630,Sweden\n" +
            "40.4525,1,New Jersey,-74.4767,United States\n" +
            "37.2710,1,Virginia,-79.9414,United States\n" +
            "45.5229,4,Oregon,-122.9898,United States\n" +
            "34.0234,5,Georgia,-84.6155,United States\n" +
            "33.9898,1,California,-117.7326,United States\n" +
            "53.7486,1,England,-2.4875,United Kingdom\n" +
            "30.3960,1,Mississippi,-88.8853,United States\n" +
            "40.1740,1,Pennsylvania,-80.2462,United States\n" +
            "30.6280,3,Texas,-96.3344,United States\n" +
            "43.3361,1,Provence-Alpes-Cote d'Azur,5.4826,France\n" +
            "44.6497,2,Minnesota,-93.2427,United States\n" +
            "20.7910,0,Hawaii,-156.3269,United States\n" +
            "40.8582,1,New Jersey,-74.0807,United States\n" +
            "0.0000,0,(not set),0.0000,Bermuda\n" +
            "55.7303,1,Capital Region of Denmark,12.4249,Denmark\n" +
            "32.3415,0,Mississippi,-90.3218,United States\n" +
            "26.1224,6,Florida,-80.1373,United States\n" +
            "30.3165,1,Uttarakhand,78.0322,India\n" +
            "62.4540,1,Northwest Territories,-114.3718,Canada\n" +
            "43.6121,2,Idaho,-116.3915,United States\n" +
            "0.0000,2,Lisbon,0.0000,Portugal\n" +
            "44.3894,2,Ontario,-79.6903,Canada\n" +
            "45.0703,1,Piedmont,7.6869,Italy\n" +
            "49.1666,2,British Columbia,-123.1336,Canada\n" +
            "41.1400,0,Wyoming,-104.8202,United States\n" +
            "45.8202,2,Michigan,-88.0660,United States\n" +
            "0.0000,2,Lithuania,0.0000,Lithuania\n" +
            "27.7000,1,(not set),85.3333,Nepal\n" +
            "24.9157,1,(not set),121.6739,Taiwan\n";

    public static final String GEO4 =
            "0.0000,2,Latvia,0.0000,Latvia\n" +
            "33.0343,1,Georgia,-83.9382,United States\n" +
            "39.6868,3,Delaware,-75.5844,United States\n" +
            "42.1399,6,Massachusetts,-71.5163,United States\n" +
            "44.3500,1,Ontario,-78.7500,Canada\n" +
            "55.0084,1,Novosibirsk Oblast,82.9357,Russia\n" +
            "54.5973,2,Northern Ireland,-5.9301,United Kingdom\n" +
            "49.5897,1,Bavaria,11.0120,Germany\n" +
            "39.2870,1,Ohio,-84.4852,United States\n" +
            "-36.3797,0,Victoria,145.4250,Australia\n" +
            "33.1581,17,California,-117.3506,United States\n" +
            "25.0911,2,(not set),121.5598,Taiwan\n" +
            "34.0232,5,Georgia,-84.3616,United States\n" +
            "39.9612,6,Ohio,-82.9988,United States\n" +
            "29.9988,1,Texas,-95.1766,United States\n" +
            "48.7463,0,Ile-de-France,2.2668,France\n" +
            "38.9907,2,Maryland,-77.0261,United States\n" +
            "44.3380,1,(not set),19.7188,Serbia\n" +
            "33.4936,1,California,-117.1484,United States\n" +
            "36.2554,1,Arkansas,-94.1308,United States\n" +
            "40.0525,1,Pennsylvania,-75.6557,United States\n" +
            "34.5034,1,South Carolina,-82.6501,United States\n" +
            "51.0543,3,East Flanders,3.7174,Belgium\n" +
            "47.6262,1,Washington,-122.5212,United States\n" +
            "51.9851,1,Gelderland,5.8987,Netherlands\n" +
            "-35.2820,9,Australian Capital Territory,149.1287,Australia\n" +
            "45.4655,1,Lombardy,9.1865,Italy\n" +
            "42.5584,0,Massachusetts,-71.2689,United States\n" +
            "51.2362,2,England,-0.5704,United Kingdom\n" +
            "39.8468,2,Pennsylvania,-75.7116,United States\n" +
            "39.7876,1,Delaware,-75.6966,United States\n" +
            "50.0405,1,Alberta,-110.6764,Canada\n" +
            "41.8661,3,Illinois,-88.1070,United States\n" +
            "51.4315,6,England,-0.7881,United Kingdom\n" +
            "51.8234,2,England,-1.2905,United Kingdom\n" +
            "0.0000,1,(not set),0.0000,Jordan\n" +
            "69.9550,0,Finnmark,23.3279,Norway\n" +
            "50.0782,1,Hesse,8.2398,Germany\n" +
            "30.0131,0,Cairo Governorate,31.2089,Egypt\n" +
            "44.6264,1,Wisconsin,-91.9657,United States\n" +
            "40.6301,0,Pennsylvania,-79.9703,United States\n" +
            "39.9168,1,Pennsylvania,-75.3877,United States\n" +
            "40.3769,1,Utah,-111.7958,United States\n" +
            "41.6130,1,Massachusetts,-70.9705,United States\n" +
            "14.1877,1,Calabarzon,121.1251,Philippines\n" +
            "35.2220,1,Texas,-101.8313,United States\n" +
            "22.5726,6,West Bengal,88.3639,India\n" +
            "26.0078,0,Florida,-80.2963,United States\n" +
            "40.4190,1,West Virginia,-80.5895,United States\n" +
            "42.5630,1,Idaho,-114.4609,United States\n" +
            "41.0998,1,Ohio,-80.6495,United States\n" +
            "41.6215,1,Connecticut,-72.7457,United States\n" +
            "39.7817,1,Illinois,-89.6502,United States\n" +
            "41.1839,1,Nebraska,-96.1525,United States\n" +
            "34.1361,2,California,-117.8653,United States\n" +
            "19.4326,15,Federal District,-99.1332,Mexico\n" +
            "28.0781,1,Florida,-82.7637,United States\n" +
            "33.1507,2,Texas,-96.8236,United States\n" +
            "54.6872,3,(not set),25.2797,Lithuania\n" +
            "60.0971,1,(not set),19.9348,Åland Islands\n" +
            "57.7089,7,Vastra Gotaland County,11.9746,Sweden\n" +
            "51.6565,2,England,-0.3903,United Kingdom\n" +
            "33.5179,0,Georgia,-84.6697,United States\n" +
            "0.0000,1,Mongolia,0.0000,Mongolia\n" +
            "42.9814,5,New Hampshire,-70.9478,United States\n" +
            "41.8800,1,Illinois,-88.0078,United States\n" +
            "39.1434,2,Maryland,-77.2014,United States\n" +
            "0.0000,1,West Virginia,0.0000,United States\n" +
            "52.1764,1,Utrecht,5.2992,Netherlands\n" +
            "44.9747,0,Wisconsin,-92.7569,United States\n" +
            "37.3394,18,California,-121.8950,United States\n" +
            "38.8048,17,Virginia,-77.0469,United States\n" +
            "49.2831,1,British Columbia,-122.8317,Canada\n" +
            "35.8235,2,North Carolina,-78.8256,United States\n" +
            "20.9206,3,Hawaii,-156.3125,United States\n" +
            "52.0798,1,Utrecht,4.8627,Netherlands\n" +
            "40.5853,1,Colorado,-105.0844,United States\n" +
            "45.0051,1,Oregon,-122.7831,United States\n" +
            "40.7144,20,New York,-74.0060,United States\n" +
            "45.2879,1,Oregon,-122.5353,United States\n" +
            "-23.4210,1,Parana,-51.9331,Brazil\n" +
            "40.6850,2,Pennsylvania,-80.1071,United States\n" +
            "44.0234,2,Minnesota,-92.4630,United States\n" +
            "56.3274,1,Nizhny Novgorod Oblast,43.9852,Russia\n" +
            "33.2098,2,Alabama,-87.5692,United States\n" +
            "51.7075,0,England,-1.7851,United Kingdom\n" +
            "32.6245,2,Baja California,-115.4523,Mexico\n" +
            "49.4771,2,Bavaria,10.9887,Germany\n" +
            "43.1009,1,New York,-75.2327,United States\n" +
            "46.7208,1,Wisconsin,-92.1041,United States\n" +
            "47.6779,1,Baden-Wurttemberg,9.1732,Germany\n" +
            "39.9853,2,Colorado,-104.8205,United States\n" +
            "44.8015,2,Emilia-Romagna,10.3279,Italy\n" +
            "45.8803,1,Quebec,-72.4843,Canada\n" +
            "35.8456,3,Tennessee,-86.3903,United States\n" +
            "52.7055,1,North Holland,4.7053,Netherlands\n" +
            "42.8864,2,New York,-78.8784,United States\n" +
            "51.6410,1,North Brabant,4.8617,Netherlands\n" +
            "51.9038,1,England,-0.1966,United Kingdom\n" +
            "33.4487,4,Georgia,-84.4549,United States\n" +
            "40.4350,2,(not set),49.8676,Azerbaijan\n" +
            "35.4676,14,Oklahoma,-97.5164,United States\n" +
            "51.3197,1,North Brabant,5.3575,Netherlands\n" +
            "42.8886,1,Wisconsin,-88.0384,United States\n" +
            "39.3601,15,Ohio,-84.3099,United States\n" +
            "39.9040,1,Beijing,116.4075,China\n" +
            "39.2904,4,Maryland,-76.6122,United States\n" +
            "42.3736,6,Massachusetts,-71.1097,United States\n" +
            "41.3898,1,Ohio,-81.4412,United States\n" +
            "35.5085,2,North Carolina,-78.3394,United States\n" +
            "35.1740,0,North Carolina,-79.3923,United States\n" +
            "28.4145,1,Florida,-81.4428,United States\n" +
            "35.6528,4,Oklahoma,-97.4781,United States\n" +
            "0.0000,1,Apulia,0.0000,Italy\n" +
            "43.9167,1,Ontario,-80.8667,Canada\n" +
            "37.9717,1,Kansas,-100.8727,United States\n" +
            "34.1322,2,Georgia,-84.3007,United States\n" +
            "40.8373,3,Ohio,-81.2596,United States\n" +
            "51.7532,1,England,-0.4486,United Kingdom\n" +
            "56.8339,1,Kronoberg County,13.9410,Sweden\n" +
            "47.0343,1,Washington,-122.8232,United States\n" +
            "54.0466,1,England,-2.8007,United Kingdom\n" +
            "42.3765,3,Massachusetts,-71.2356,United States\n" +
            "52.1561,1,Utrecht,5.3878,Netherlands\n" +
            "56.4606,1,Central Denmark Region,10.0365,Denmark\n" +
            "38.9807,1,Virginia,-77.5282,United States\n" +
            "6.1667,2,Antioquia,-75.5667,Colombia\n" +
            "33.5750,2,California,-117.7256,United States\n" +
            "39.7684,11,Indiana,-86.1581,United States\n" +
            "-37.3178,1,Buenos Aires Province,-59.1504,Argentina\n" +
            "41.2307,1,Connecticut,-73.0640,United States\n" +
            "52.3082,11,Overijssel,6.5213,Netherlands\n" +
            "33.8675,1,California,-117.9981,United States\n" +
            "39.7029,5,New Jersey,-75.1118,United States\n" +
            "33.4356,1,Arizona,-112.3496,United States\n" +
            "21.3996,1,Hawaii,-157.7974,United States\n" +
            "40.4168,12,Community of Madrid,-3.7038,Spain\n" +
            "52.3061,1,North Holland,4.6907,Netherlands\n" +
            "43.0517,1,Wisconsin,-91.1412,United States\n" +
            "52.5695,1,England,-0.2405,United Kingdom\n" +
            "38.8943,2,Virginia,-77.4311,United States\n" +
            "41.8724,4,Lazio,12.4802,Italy\n" +
            "42.5803,2,Michigan,-83.0302,United States\n" +
            "43.1340,1,New Hampshire,-70.9264,United States\n" +
            "0.0000,0,Phichit,0.0000,Thailand\n" +
            "-2.2038,3,Guayas,-79.8975,Ecuador\n" +
            "45.1406,1,Ontario,-76.1465,Canada\n" +
            "-20.1667,3,(not set),57.5167,Mauritius\n" +
            "34.4358,3,California,-119.8276,United States\n" +
            "51.4963,7,North Rhine-Westphalia,6.8638,Germany\n" +
            "19.9975,1,Maharashtra,73.7898,India\n" +
            "42.4793,1,Massachusetts,-71.1523,United States\n" +
            "38.9072,10,District of Columbia,-77.0369,United States\n" +
            "24.4667,2,Abu Dhabi,54.3667,United Arab Emirates\n" +
            "51.2665,3,England,-1.0924,United Kingdom\n" +
            "37.2698,1,West Virginia,-81.2223,United States\n" +
            "42.5467,1,Michigan,-83.2113,United States\n" +
            "38.6654,1,Virginia,-78.4594,United States\n" +
            "40.0334,1,Ohio,-83.1582,United States\n" +
            "33.8920,1,Georgia,-84.2988,United States\n" +
            "51.8969,0,Cork City,-8.4863,Ireland\n" +
            "42.0884,1,Illinois,-87.9806,United States\n" +
            "45.7533,2,Quebec,-73.4401,Canada\n" +
            "32.9756,3,Texas,-96.8900,United States\n" +
            "-25.8603,57,Gauteng,28.1894,South Africa\n" +
            "40.8596,3,New Jersey,-74.4233,United States\n" +
            "28.6700,1,Florida,-81.2081,United States\n" +
            "45.0861,1,Minnesota,-93.2633,United States\n" +
            "37.7719,1,Kentucky,-87.1112,United States\n" +
            "-32.8902,1,Mendoza Province,-68.8440,Argentina\n" +
            "36.2168,1,North Carolina,-81.6746,United States\n" +
            "45.6984,1,Lombardy,9.4065,Italy\n" +
            "42.1945,1,Massachusetts,-71.8356,United States\n" +
            "44.2312,1,Ontario,-76.4860,Canada\n" +
            "0.0000,1,(not set),0.0000,Rwanda\n" +
            "40.7144,1,(not set),-74.0060,United States\n" +
            "45.4397,1,Rhone-Alpes,4.3872,France\n" +
            "8.9833,2,(not set),-79.5167,Panama\n" +
            "42.2959,1,Massachusetts,-71.7128,United States\n" +
            "60.2935,4,(not set),25.0377,Finland\n" +
            "41.0887,1,New York,-74.0135,United States\n" +
            "33.9304,5,Georgia,-84.3733,United States\n" +
            "33.7581,7,South Carolina,-78.9669,United States\n" +
            "28.0395,2,(not set),-81.9498,United States\n" +
            "23.2599,1,Madhya Pradesh,77.4126,India\n" +
            "50.4333,2,(not set),80.2667,Kazakhstan\n" +
            "41.5895,1,Illinois,-88.0578,United States\n" +
            "44.3256,2,Ontario,-79.8885,Canada\n" +
            "39.4562,1,West Virginia,-77.9639,United States\n" +
            "52.4560,1,England,-1.1992,United Kingdom\n" +
            "45.7754,1,Quebec,-74.0049,Canada\n" +
            "32.8140,8,Texas,-96.9489,United States\n" +
            "52.1982,1,North Rhine-Westphalia,8.5832,Germany\n" +
            "34.0625,4,California,-118.1228,United States\n" +
            "38.9847,2,Maryland,-77.0947,United States\n" +
            "37.8716,4,California,-122.2727,United States\n" +
            "28.6353,8,Delhi,77.2250,India\n" +
            "37.9358,0,California,-122.3477,United States\n" +
            "35.1112,1,South Carolina,-81.2265,United States\n" +
            "41.7000,1,Massachusetts,-70.7633,United States\n" +
            "27.4953,1,Florida,-82.7109,United States\n" +
            "47.0379,1,Washington,-122.9007,United States\n" +
            "38.6126,1,Missouri,-90.3246,United States\n" +
            "43.5325,1,Wisconsin,-90.0026,United States\n" +
            "44.1529,1,Ontario,-79.8686,Canada\n" +
            "49.4432,0,Upper Normandy,1.1000,France\n" +
            "37.8667,1,Konya Province,32.4833,Turkey\n" +
            "41.7245,2,Ohio,-81.2457,United States\n" +
            "39.9438,1,Pennsylvania,-75.3937,United States\n" +
            "35.6640,1,Tokyo,139.6982,Japan\n" +
            "51.2194,1,Antwerp,4.4025,Belgium\n" +
            "-22.9099,1,Sao Paulo,-47.0626,Brazil\n" +
            "48.8932,1,Ile-de-France,2.2879,France\n" +
            "-45.8788,1,Otago,170.5028,New Zealand\n" +
            "27.9506,2,Florida,-82.4572,United States\n" +
            "37.3797,0,California,-122.1375,United States\n" +
            "37.6624,4,California,-121.8747,United States\n" +
            "39.1592,7,Ohio,-84.3072,United States\n" +
            "38.5449,1,California,-121.7405,United States\n" +
            "40.3659,1,New Jersey,-74.9429,United States\n" +
            "0.0000,1,Dominican Republic,0.0000,Dominican Republic\n" +
            "51.8185,1,England,-0.3590,United Kingdom\n" +
            "42.2631,3,Illinois,-88.0040,United States\n" +
            "4.6118,1,Perak,101.1135,Malaysia\n" +
            "29.0283,1,Florida,-81.3031,United States\n" +
            "40.4233,1,Colorado,-104.7091,United States\n" +
            "30.4763,0,Alabama,-88.3422,United States\n" +
            "48.5831,2,Alsace,7.7479,France\n" +
            "36.0252,0,Nevada,-115.2419,United States\n" +
            "-12.2669,1,Bahia,-38.9666,Brazil\n" +
            "14.5995,3,Metro Manila,120.9842,Philippines\n" +
            "41.5623,4,Connecticut,-72.6507,United States\n" +
            "37.9839,8,Athens,23.7294,Greece\n" +
            "0.0000,2,(not set),0.0000,Kenya\n" +
            "55.0938,0,Moskovskaya oblast,38.7689,Russia\n" +
            "36.1370,0,Nevada,-115.1186,United States\n" +
            "-6.8992,2,Paraiba,-38.5270,Brazil\n" +
            "34.8526,2,South Carolina,-82.3940,United States\n" +
            "41.7370,3,Utah,-111.8338,United States\n" +
            "-7.9839,1,East Java,112.6214,Indonesia\n" +
            "43.0592,1,New York,-77.6128,United States\n" +
            "43.0020,1,Michigan,-85.5715,United States\n" +
            "42.0000,1,(not set),21.4333,Macedonia (FYROM)\n" +
            "38.3032,1,Virginia,-77.4605,United States\n" +
            "22.7196,1,Madhya Pradesh,75.8577,India\n" +
            "50.3569,1,Rhineland-Palatinate,7.5890,Germany\n" +
            "33.2487,2,Arizona,-111.6343,United States\n" +
            "38.2098,1,Kentucky,-84.5588,United States\n" +
            "21.3069,2,Hawaii,-157.8583,United States\n" +
            "41.6267,6,Iowa,-93.7122,United States\n" +
            "7.1907,3,Davao Region,125.4553,Philippines\n" +
            "45.8150,6,City of Zagreb,15.9819,Croatia\n" +
            "41.6884,0,Iowa,-93.7925,United States\n" +
            "0.0000,1,(not set),0.0000,Slovenia\n" +
            "45.4010,2,Quebec,-71.8824,Canada\n" +
            "44.8114,4,Wisconsin,-91.4985,United States\n" +
            "38.8792,2,Kansas,-99.3268,United States\n" +
            "47.1936,1,Canton of Solothurn,7.3954,Switzerland\n" +
            "51.3614,1,England,-0.1940,United Kingdom\n" +
            "-33.4691,11,Santiago Metropolitan Region,-70.6420,Chile\n" +
            "45.2998,1,Oregon,-122.7737,United States\n" +
            "12.9165,1,Tamil Nadu,79.1325,India\n" +
            "48.8397,3,Ile-de-France,2.2399,France\n" +
            "22.2800,10,(not set),114.1588,Hong Kong\n" +
            "52.1917,2,England,-1.7083,United Kingdom\n" +
            "44.9239,1,Minnesota,-92.9594,United States\n" +
            "-6.1783,0,Banten,106.6319,Indonesia\n" +
            "34.2164,1,California,-119.0376,United States\n" +
            "40.5492,1,Ohio,-82.8274,United States\n" +
            "48.9142,1,Ile-de-France,2.2854,France\n" +
            "36.9685,1,Kentucky,-86.4808,United States\n" +
            "38.7329,1,Virginia,-77.0580,United States\n" +
            "0.0000,1,(not set),0.0000,Iran\n" +
            "40.4406,8,Pennsylvania,-79.9959,United States\n" +
            "33.5779,3,Texas,-101.8552,United States\n" +
            "37.4241,0,California,-122.1661,United States\n" +
            "-31.9530,13,Western Australia,115.8575,Australia\n" +
            "33.6839,17,California,-117.7947,United States\n" +
            "1.2801,20,(not set),103.8510,Singapore\n" +
            "65.0121,1,(not set),25.4650,Finland\n" +
            "39.3279,1,New Jersey,-74.5035,United States\n" +
            "36.9290,2,Missouri,-93.9277,United States\n" +
            "39.4322,0,Valencian Community,-0.4724,Spain\n" +
            "56.2601,0,Central Denmark Region,10.2999,Denmark\n" +
            "32.0853,19,Tel Aviv District,34.7818,Israel\n" +
            "37.2090,6,Missouri,-93.2923,United States\n" +
            "47.5605,3,Newfoundland and Labrador,-52.7128,Canada\n" +
            "44.9917,1,Minnesota,-93.3600,United States\n" +
            "48.0061,1,Pays de la Loire,0.1996,France\n" +
            "37.7022,1,California,-121.9358,United States\n" +
            "42.6220,1,New York,-73.8326,United States\n" +
            "43.7428,1,Ontario,-81.7139,Canada\n" +
            "41.7859,6,Illinois,-88.1473,United States\n" +
            "27.8428,1,Florida,-82.6995,United States\n" +
            "24.6333,2,Riyadh Province,46.7167,Saudi Arabia\n" +
            "37.2276,2,Virginia,-80.0128,United States\n" +
            "34.1425,1,Arizona,-109.9604,United States\n" +
            "45.4871,4,Oregon,-122.8037,United States\n" +
            "50.8167,1,England,-1.0833,United Kingdom\n" +
            "41.4080,1,Iowa,-92.9164,United States\n" +
            "33.4718,1,Alabama,-86.8008,United States\n" +
            "38.9696,1,Virginia,-77.3861,United States\n" +
            "45.7775,1,Michigan,-84.7271,United States\n" +
            "35.4088,1,North Carolina,-80.5795,United States\n" +
            "36.1699,1,Nevada,-115.1398,United States\n" +
            "36.5101,1,Andalusia,-4.8824,Spain\n" +
            "40.6688,0,Utah,-111.8247,United States\n" +
            "51.7022,1,North Brabant,5.7292,Netherlands\n" +
            "33.6143,1,Alabama,-85.8350,United States\n" +
            "18.6298,11,Maharashtra,73.7997,India\n" +
            "-26.1625,2,Gauteng,27.8725,South Africa\n" +
            "34.2694,0,California,-118.7815,United States\n" +
            "44.7319,1,Minnesota,-93.2177,United States\n" +
            "42.9956,1,New Hampshire,-71.4548,United States\n" +
            "0.0000,1,(not set),0.0000,Netherlands\n" +
            "45.4337,0,Oregon,-122.5519,United States\n" +
            "0.0000,2,Sao Paulo,0.0000,Brazil\n" +
            "50.8424,1,England,-1.0660,United Kingdom\n" +
            "41.1158,0,Nebraska,-98.0017,United States\n" +
            "38.7726,1,Virginia,-77.2211,United States\n" +
            "42.2612,1,Massachusetts,-71.4634,United States\n" +
            "39.5758,1,Colorado,-105.1122,United States\n" +
            "-36.8485,24,Auckland,174.7633,New Zealand\n" +
            "50.8543,1,England,0.5735,United Kingdom\n" +
            "40.9913,1,New Jersey,-74.0598,United States\n" +
            "36.1867,1,Arkansas,-94.1288,United States\n" +
            "53.3811,1,England,-1.4701,United Kingdom\n" +
            "25.2000,5,Dubai,55.3000,United Arab Emirates\n" +
            "39.1429,1,Missouri,-94.5730,United States\n" +
            "40.1934,5,Indiana,-85.3864,United States\n" +
            "59.8218,2,Akershus,10.4210,Norway\n" +
            "-6.2088,6,Jakarta,106.8456,Indonesia\n" +
            "59.3293,26,Stockholm County,18.0686,Sweden\n" +
            "42.5251,1,Massachusetts,-71.7598,United States\n" +
            "46.5802,1,Poitou-Charentes,0.3404,France\n" +
            "41.5792,0,Ohio,-81.2044,United States\n" +
            "0.0000,0,(not set),0.0000,Guernsey\n" +
            "42.5063,1,(not set),1.5218,Andorra\n" +
            "39.0536,1,Maryland,-77.0592,United States\n" +
            "45.4562,1,Oregon,-123.8440,United States\n" +
            "32.9546,0,Texas,-97.0150,United States\n" +
            "53.5412,2,Alberta,-113.2957,Canada\n" +
            "49.1365,1,Baden-Wurttemberg,8.5301,Germany\n" +
            "38.9108,1,Missouri,-94.3822,United States\n" +
            "46.5456,1,Mures County,24.5625,Romania\n" +
            "53.9000,6,(not set),27.5667,Belarus\n" +
            "44.8167,7,(not set),20.4667,Serbia\n" +
            "40.3381,1,New Jersey,-74.2687,United States\n" +
            "32.7157,16,California,-117.1611,United States\n" +
            "47.8095,3,Salzburg,13.0550,Austria\n" +
            "44.7319,1,Nova Scotia,-63.6567,Canada\n" +
            "21.0333,3,Hanoi,105.8500,Vietnam\n" +
            "53.3900,4,England,-3.0230,United Kingdom\n" +
            "40.1013,1,Pennsylvania,-75.3836,United States\n" +
            "44.8408,5,Minnesota,-93.2983,United States\n" +
            "44.2911,0,Wyoming,-105.5022,United States\n" +
            "53.5444,18,Alberta,-113.4909,Canada\n" +
            "30.4583,1,Louisiana,-91.1403,United States\n" +
            "31.7700,1,Texas,-106.4968,United States\n" +
            "50.8376,1,England,-0.7749,United Kingdom\n" +
            "45.5087,12,Quebec,-73.5540,Canada\n" +
            "47.6740,6,Washington,-122.1215,United States\n" +
            "-20.8990,1,Sao Paulo,-51.3797,Brazil\n" +
            "38.9187,2,Virginia,-77.2311,United States\n" +
            "38.6270,2,Missouri,-90.1994,United States\n" +
            "28.4595,5,Haryana,77.0266,India\n" +
            "43.0978,1,New York,-76.1452,United States\n" +
            "50.7150,3,England,-1.9872,United Kingdom\n" +
            "42.4084,5,Massachusetts,-71.0120,United States\n" +
            "41.3083,4,Connecticut,-72.9279,United States\n" +
            "41.0037,1,Pennsylvania,-80.3470,United States\n" +
            "50.8188,1,Saxony,12.5452,Germany\n" +
            "52.3080,3,North Holland,4.9715,Netherlands\n" +
            "38.8339,4,Colorado,-104.8214,United States\n" +
            "52.2799,1,Lower Saxony,8.0472,Germany\n" +
            "42.9634,2,Michigan,-85.6681,United States\n" +
            "33.5539,1,California,-117.2139,United States\n" +
            "42.4529,2,New York,-75.0638,United States\n" +
            "45.6071,1,Ontario,-74.6042,Canada\n" +
            "32.8323,4,South Carolina,-79.8284,United States\n" +
            "51.4543,3,England,-0.9781,United Kingdom\n" +
            "0.0000,1,Uganda,0.0000,Uganda\n" +
            "33.4152,10,Arizona,-111.8315,United States\n" +
            "50.9375,4,North Rhine-Westphalia,6.9603,Germany\n" +
            "30.6954,2,Alabama,-88.0399,United States\n" +
            "41.2506,1,Illinois,-87.8314,United States\n" +
            "52.0705,4,South Holland,4.3007,Netherlands\n" +
            "-42.8819,1,Tasmania,147.3238,Australia\n" +
            "30.0444,1,Cairo Governorate,31.2357,Egypt\n" +
            "51.7520,2,England,-1.2577,United Kingdom\n" +
            "0.0000,4,Slovakia,0.0000,Slovakia\n" +
            "51.9302,1,Gelderland,6.3478,Netherlands\n" +
            "58.0000,1,Perm Krai,56.3167,Russia\n" +
            "57.0488,1,North Denmark Region,9.9217,Denmark\n" +
            "14.6760,16,Metro Manila,121.0437,Philippines\n" +
            "-26.3044,3,Santa Catarina,-48.8464,Brazil\n" +
            "11.0168,6,Tamil Nadu,76.9558,India\n" +
            "40.1672,3,Colorado,-105.1019,United States\n" +
            "34.7695,6,Arkansas,-92.2671,United States\n" +
            "37.6305,1,California,-122.4111,United States\n" +
            "38.4193,2,West Virginia,-82.4452,United States\n" +
            "51.3168,2,England,-0.5600,United Kingdom\n" +
            "20.6720,7,Jalisco,-103.4165,Mexico\n" +
            "61.1396,0,Oppland,10.3725,Norway\n" +
            "50.7675,2,(not set),4.4740,Belgium\n" +
            "46.8772,1,North Dakota,-96.7898,United States\n" +
            "40.7128,3,(not set),-74.0059,United States\n" +
            "29.5636,2,Texas,-95.2860,United States\n" +
            "52.0274,1,England,-1.1432,United Kingdom\n" +
            "52.5049,0,England,-2.0159,United Kingdom\n" +
            "47.6104,11,Washington,-122.2007,United States\n" +
            "42.4734,3,Michigan,-83.2219,United States\n" +
            "35.6870,3,New Mexico,-105.9378,United States\n" +
            "33.7456,0,California,-116.7161,United States\n" +
            "40.7879,4,New Jersey,-74.3882,United States\n" +
            "39.4817,0,Colorado,-106.0383,United States\n" +
            "44.9833,7,Minnesota,-93.2667,United States\n" +
            "15.1450,1,Central Luzon,120.5887,Philippines\n" +
            "26.3451,1,Florida,-80.1467,United States\n" +
            "52.0564,2,England,-2.7160,United Kingdom\n" +
            "40.5765,2,Pennsylvania,-80.0293,United States\n" +
            "41.1567,2,Porto District,-8.6239,Portugal\n" +
            "59.9891,1,Vastmanland County,15.8166,Sweden\n" +
            "51.4793,1,North Brabant,5.6570,Netherlands\n" +
            "53.3902,1,England,-2.3509,United Kingdom\n" +
            "31.2304,1,Shanghai,121.4737,China\n" +
            "-26.0936,0,Gauteng,28.0064,South Africa\n" +
            "45.5235,26,Oregon,-122.6762,United States\n" +
            "52.3702,13,North Holland,4.8952,Netherlands\n" +
            "37.2358,1,California,-121.9624,United States\n" +
            "42.0347,1,Iowa,-93.6200,United States\n" +
            "38.0293,3,Virginia,-78.4767,United States\n" +
            "33.5422,1,California,-117.7831,United States\n" +
            "39.5444,5,Colorado,-104.9681,United States\n" +
            "-18.7024,0,Minas Gerais,-47.4883,Brazil\n" +
            "0.0000,2,(not set),0.0000,Qatar\n" +
            "0.0000,0,Nord-Pas-de-Calais,0.0000,France\n" +
            "27.8397,0,Florida,-82.7912,United States\n" +
            "38.9517,2,Missouri,-92.3341,United States\n" +
            "33.9775,2,California,-118.1870,United States\n" +
            "41.6612,1,Connecticut,-72.7795,United States\n" +
            "0.0000,4,(not set),0.0000,Germany\n" +
            "-22.9957,1,Sao Paulo,-47.5061,Brazil\n" +
            "31.6035,2,Texas,-94.6555,United States\n" +
            "47.9602,1,Lower Austria,14.7728,Austria\n" +
            "51.8136,1,Gelderland,5.2508,Netherlands\n" +
            "48.1987,1,Washington,-122.1251,United States\n" +
            "54.2831,1,England,-0.3998,United Kingdom\n" +
            "-22.9083,8,Rio de Janeiro,-43.1970,Brazil\n" +
            "33.9412,1,Georgia,-84.2135,United States\n" +
            "52.2112,1,Gelderland,5.9699,Netherlands\n" +
            "0.0000,7,(not set),0.0000,United States\n" +
            "51.8969,2,(not set),-8.4863,Ireland\n" +
            "44.4225,1,Ontario,-81.4035,Canada\n" +
            "15.4989,2,Goa,73.8278,India\n" +
            "-24.5290,1,Parana,-53.0029,Brazil\n" +
            "41.5122,0,Connecticut,-72.9036,United States\n" +
            "-3.7319,1,Ceara,-38.5267,Brazil\n" +
            "40.3134,936,New Jersey,-74.5202,United States\n" +
            "40.9732,1,New Jersey,-73.9615,United States\n" +
            "39.7484,1,New Jersey,-75.0691,United States\n" +
            "55.7348,1,(not set),24.3575,Lithuania\n" +
            "39.6172,1,Colorado,-104.9508,United States\n" +
            "41.0778,1,Catalonia,1.1315,Spain\n" +
            "26.6618,1,Florida,-80.2684,United States\n" +
            "39.7376,51,Colorado,-104.9847,United States\n" +
            "47.6588,3,Washington,-117.4260,United States\n" +
            "42.5793,2,Michigan,-83.2827,United States\n" +
            "36.0671,0,Shandong,120.3826,China\n" +
            "28.6611,0,Florida,-81.3656,United States\n" +
            "54.9527,1,England,-1.6034,United Kingdom\n" +
            "0.0000,3,Estonia,0.0000,Estonia\n" +
            "20.6597,2,Jalisco,-103.3496,Mexico\n" +
            "41.4784,1,Ohio,-81.4637,United States\n" +
            "39.2051,0,Maryland,-76.6527,United States\n" +
            "39.9682,0,Pennsylvania,-75.5727,United States\n" +
            "43.0023,1,Wisconsin,-89.4241,United States\n" +
            "51.5889,1,England,-1.4265,United Kingdom\n" +
            "42.7425,1,New Hampshire,-71.5895,United States\n" +
            "51.9341,1,Gelderland,5.5720,Netherlands\n" +
            "44.1636,2,Minnesota,-93.9994,United States\n" +
            "37.9735,2,California,-122.5311,United States\n" +
            "33.9462,4,Georgia,-84.3346,United States\n" +
            "0.0000,3,Slovenia,0.0000,Slovenia\n" +
            "9.9800,2,Kerala,76.2800,India\n" +
            "37.6841,1,Virginia,-77.8854,United States\n" +
            "18.6453,3,Campeche,-91.8303,Mexico\n" +
            "39.7309,2,Delaware,-75.7041,United States\n" +
            "43.6187,3,Idaho,-116.2146,United States\n" +
            "63.2970,0,Sor-Trondelag,9.7156,Norway\n" +
            "19.0760,14,Maharashtra,72.8777,India\n" +
            "45.6495,2,Friuli-Venezia Giulia,13.7768,Italy\n" +
            "40.6936,1,Illinois,-89.5890,United States\n" +
            "53.4793,4,England,-2.2485,United Kingdom\n" +
            "42.5584,3,Massachusetts,-70.8801,United States\n" +
            "-15.7942,2,Federal District,-47.8825,Brazil\n" +
            "48.8566,30,Ile-de-France,2.3522,France\n" +
            "43.1689,1,Michigan,-86.2639,United States\n" +
            "41.0793,4,Indiana,-85.1394,United States\n" +
            "36.3048,2,Tennessee,-86.6200,United States\n" +
            "41.0251,0,Ohio,-80.7609,United States\n" +
            "52.2085,3,North Rhine-Westphalia,8.8010,Germany\n" +
            "39.1911,1,Colorado,-106.8175,United States\n" +
            "38.7223,5,Lisbon,-9.1393,Portugal\n" +
            "26.9342,1,Florida,-80.0942,United States\n" +
            "42.4889,2,Iowa,-96.0725,United States\n" +
            "44.8547,7,Minnesota,-93.4708,United States\n" +
            "43.9748,1,(not set),-75.9108,United States\n" +
            "1.4634,1,Johor,103.7547,Malaysia\n" +
            "32.9483,4,Texas,-96.7299,United States\n" +
            "40.6266,1,Pennsylvania,-80.0550,United States\n" +
            "45.4392,0,Quebec,-73.2902,Canada\n" +
            "36.4615,2,North Carolina,-77.6541,United States\n" +
            "41.2401,4,Ohio,-81.4407,United States\n" +
            "30.1658,5,Texas,-95.4613,United States\n" +
            "32.7555,23,Texas,-97.3308,United States\n" +
            "33.2468,0,Georgia,-84.2641,United States\n" +
            "30.3119,1,Texas,-95.4561,United States\n" +
            "35.9940,42,North Carolina,-78.8986,United States\n" +
            "30.3061,1,Texas,-97.9524,United States\n" +
            "37.8869,1,California,-122.2978,United States\n" +
            "39.3722,1,Colorado,-104.8561,United States\n" +
            "33.9617,1,California,-118.3531,United States\n" +
            "39.5087,1,Maryland,-76.3290,United States\n" +
            "42.5000,14,Iowa,-96.4003,United States\n" +
            "45.0333,1,Krasnodar Krai,38.9667,Russia\n" +
            "53.5511,6,Hamburg,9.9937,Germany\n" +
            "42.9151,1,New York,-73.8051,United States\n" +
            "27.7731,1,Florida,-82.6400,United States\n" +
            "-41.2865,2,Wellington,174.7762,New Zealand\n" +
            "51.0259,1,(not set),4.4775,Belgium\n" +
            "40.5142,1,Illinois,-88.9906,United States\n" +
            "-23.2201,1,Rio de Janeiro,-44.7205,Brazil\n" +
            "0.0000,1,Espirito Santo,0.0000,Brazil\n" +
            "0.0000,1,Tunisia,0.0000,Tunisia\n" +
            "32.2217,3,Arizona,-110.9265,United States\n" +
            "0.0000,0,Iceland,0.0000,Iceland\n" +
            "33.2000,1,California,-117.2425,United States\n" +
            "42.3703,1,Illinois,-87.9020,United States\n" +
            "34.2856,1,California,-118.8820,United States\n" +
            "37.5665,3,Seoul,126.9780,South Korea\n" +
            "32.9126,1,Texas,-96.6389,United States\n" +
            "34.2542,0,Arizona,-110.0298,United States\n" +
            "35.6678,1,Texas,-101.3974,United States\n" +
            "31.2232,0,Alabama,-85.3905,United States\n" +
            "0.0000,5,Parana,0.0000,Brazil\n" +
            "40.5187,1,Pennsylvania,-78.3947,United States\n" +
            "41.6718,2,Connecticut,-72.9493,United States\n" +
            "51.5105,2,England,-0.5950,United Kingdom\n" +
            "20.6900,3,Hawaii,-156.4392,United States\n" +
            "55.6050,2,Skane County,13.0038,Sweden\n" +
            "45.1608,2,Minnesota,-93.2349,United States\n" +
            "42.7325,5,Michigan,-84.5555,United States\n" +
            "52.5870,1,England,-2.1288,United Kingdom\n" +
            "44.6082,0,Ontario,-79.4197,Canada\n" +
            "44.0521,2,Oregon,-123.0868,United States\n" +
            "31.6074,1,Georgia,-81.8854,United States\n" +
            "36.1628,2,Tennessee,-85.5016,United States\n" +
            "50.9860,1,(not set),4.8365,Belgium\n" +
            "50.9307,1,(not set),5.3325,Belgium\n" +
            "40.9312,1,New York,-73.8987,United States\n" +
            "15.4930,1,Goa,73.8180,India\n" +
            "0.0000,1,Indiana,0.0000,United States\n" +
            "28.9652,2,Canary Islands,-13.5550,Spain\n" +
            "45.8182,1,(not set),13.7566,Slovenia\n" +
            "46.0065,1,Ticino,8.9523,Switzerland\n" +
            "53.8008,5,England,-1.5491,United Kingdom\n" +
            "39.1732,1,Maryland,-77.2716,United States\n" +
            "42.9922,1,Ontario,-79.2483,Canada\n" +
            "0.0000,1,California,0.0000,United States\n" +
            "48.5285,1,Alsace,7.7110,France\n" +
            "28.6278,2,Florida,-81.3631,United States\n" +
            "30.4213,2,Florida,-87.2169,United States\n" +
            "32.9343,4,Texas,-97.0781,United States\n" +
            "50.8323,1,Saxony,12.9186,Germany\n" +
            "10.6596,1,(not set),-61.4789,Trinidad and Tobago\n" +
            "34.1706,2,California,-118.8376,United States\n" +
            "50.8366,1,Brussels,4.3082,Belgium\n" +
            "58.5380,1,Ostergotland County,15.0471,Sweden\n" +
            "50.3755,1,England,-4.1427,United Kingdom\n" +
            "43.0125,1,Michigan,-83.6875,United States\n" +
            "39.1626,1,Maryland,-76.6247,United States\n" +
            "40.6677,2,Utah,-111.9388,United States\n" +
            "34.2570,1,Georgia,-85.1647,United States\n" +
            "44.9597,4,Minnesota,-93.3702,United States\n" +
            "46.3069,1,Burgundy,4.8287,France\n" +
            "41.2667,1,(not set),69.2167,Uzbekistan\n" +
            "26.1723,0,Florida,-80.1320,United States\n" +
            "42.4595,1,Michigan,-83.1827,United States\n" +
            "11.5449,2,(not set),104.8922,Cambodia\n" +
            "26.4615,1,Florida,-80.0728,United States\n" +
            "39.0576,1,Maryland,-76.9358,United States\n" +
            "52.2875,1,North Holland,4.7530,Netherlands\n" +
            "37.2296,0,Virginia,-80.4139,United States\n" +
            "30.6610,1,Mississippi,-88.4942,United States\n" +
            "53.5428,1,Alberta,-113.9034,Canada\n" +
            "39.4301,1,West Virginia,-77.8042,United States\n" +
            "36.0331,3,Tennessee,-86.7828,United States\n" +
            "34.4839,1,Arizona,-114.3225,United States\n" +
            "0.0000,2,Catalonia,0.0000,Spain\n" +
            "39.0840,4,Maryland,-77.1528,United States\n" +
            "31.3382,1,Texas,-94.7291,United States\n" +
            "28.7728,1,Florida,-81.3714,United States\n" +
            "39.7439,1,Ohio,-84.6366,United States\n" +
            "26.1420,0,Florida,-81.7948,United States\n" +
            "14.5764,0,Metro Manila,121.0851,Philippines\n" +
            "34.9204,1,South Carolina,-82.2962,United States\n" +
            "0.0000,2,Guatemala,0.0000,Guatemala\n" +
            "17.3850,6,(not set),78.4867,India\n" +
            "60.1237,1,(not set),24.4414,Finland\n" +
            "42.5987,1,Castile and Leon,-5.5671,Spain\n" +
            "51.5224,1,England,-0.7219,United Kingdom\n" +
            "34.0736,1,California,-118.4004,United States\n" +
            "32.7801,22,Texas,-96.8004,United States\n" +
            "40.8894,1,Utah,-111.8808,United States\n" +
            "40.9076,1,Campania,14.2928,Italy\n" +
            "-20.2976,0,Espirito Santo,-40.2958,Brazil\n" +
            "40.8862,2,New York,-73.2573,United States\n" +
            "51.1857,1,England,-0.6128,United Kingdom\n" +
            "40.0793,0,Pennsylvania,-75.3016,United States\n" +
            "51.8630,5,England,-2.4903,United Kingdom\n" +
            "39.8309,1,Pennsylvania,-77.2311,United States\n" +
            "53.4513,1,Kuyavian-Pomeranian Voivodeship,17.5316,Poland\n" +
            "43.8828,2,Ontario,-79.4403,Canada\n" +
            "47.2265,2,Zurich,8.6696,Switzerland\n" +
            "5.7167,0,Boyaca,-72.9167,Colombia\n" +
            "49.1833,3,British Columbia,-122.8500,Canada\n" +
            "45.0164,1,Ontario,-75.6459,Canada\n" +
            "60.9827,1,(not set),25.6612,Finland\n" +
            "37.7139,0,Kansas,-97.1364,United States\n" +
            "-12.9722,1,Bahia,-38.5014,Brazil\n" +
            "42.5349,1,Iowa,-92.4453,United States\n" +
            "35.2271,21,North Carolina,-80.8431,United States\n" +
            "38.7384,1,Virginia,-77.1850,United States\n" +
            "41.4828,0,Indiana,-87.3328,United States\n" +
            "19.4833,1,State of Mexico,-99.2333,Mexico\n" +
            "40.6404,1,Basilicata,15.8056,Italy\n" +
            "40.7961,1,New Jersey,-74.4938,United States\n" +
            "38.2589,1,Maryland,-76.4506,United States\n" +
            "32.4610,1,Georgia,-84.9877,United States\n" +
            "0.0000,1,Massachusetts,0.0000,United States\n" +
            "0.0000,3,Georgia,0.0000,United States\n" +
            "-23.5114,3,Sao Paulo,-46.8729,Brazil\n" +
            "41.4353,1,Ohio,-81.6573,United States\n" +
            "48.0794,1,Alsace,7.3585,France\n" +
            "28.6103,1,Florida,-81.2887,United States\n" +
            "45.4384,1,Veneto,10.9916,Italy\n" +
            "34.0953,2,California,-118.1270,United States\n" +
            "7.7667,1,Tachira,-72.2333,Venezuela\n" +
            "38.5518,1,California,-121.3647,United States\n" +
            "34.9496,0,South Carolina,-81.9321,United States\n" +
            "33.0146,2,Texas,-97.0970,United States\n" +
            "40.0192,0,Ohio,-82.8793,United States\n" +
            "52.9847,1,Lower Saxony,9.8421,Germany\n" +
            "-29.6006,16,KwaZulu-Natal,30.3794,South Africa\n" +
            "41.1148,1,New York,-74.1496,United States\n" +
            "53.2012,1,Friesland,5.7999,Netherlands\n" +
            "29.6486,2,Florida,-81.6376,United States\n" +
            "48.4757,1,Washington,-122.3254,United States\n" +
            "36.7667,0,Antalya Province,31.3889,Turkey\n" +
            "34.9618,1,Mississippi,-89.8295,United States\n" +
            "39.9205,4,Colorado,-105.0866,United States\n" +
            "40.3553,4,Pennsylvania,-80.0495,United States\n" +
            "44.9530,3,Minnesota,-92.9952,United States\n" +
            "51.2322,1,England,-0.3324,United Kingdom\n" +
            "25.8651,1,Florida,-80.3245,United States\n" +
            "32.7920,1,California,-115.5630,United States\n" +
            "36.3134,1,Tennessee,-82.3535,United States\n" +
            "40.6669,0,Utah,-111.8880,United States\n" +
            "36.0972,2,Nevada,-115.1467,United States\n" +
            "34.1478,0,California,-118.1445,United States\n" +
            "40.1215,1,Pennsylvania,-75.3399,United States\n" +
            "42.0333,1,Illinois,-88.2833,United States\n" +
            "41.3139,1,Ohio,-81.6851,United States\n" +
            "25.9861,1,Florida,-80.3036,United States\n" +
            "39.3911,1,New Jersey,-74.5939,United States\n" +
            "40.2091,1,New Jersey,-74.0386,United States\n" +
            "41.5772,2,Iowa,-93.7113,United States\n" +
            "58.4108,1,Ostergotland County,15.6214,Sweden\n" +
            "40.3356,1,Pennsylvania,-75.9269,United States\n" +
            "50.1109,1,Hesse,8.6821,Germany\n" +
            "23.0225,7,Gujarat,72.5714,India\n" +
            "40.6599,3,Utah,-111.9963,United States\n" +
            "34.1064,1,California,-117.5931,United States\n" +
            "59.5670,0,Akershus,10.6851,Norway\n" +
            "-29.6873,1,Rio Grande do Sul,-53.8154,Brazil\n" +
            "25.3818,1,Sindh,68.3694,Pakistan\n" +
            "29.9511,4,Louisiana,-90.0715,United States\n" +
            "38.8697,2,Colorado,-106.9878,United States\n" +
            "52.1862,1,South Holland,4.4748,Netherlands\n" +
            "49.1042,1,British Columbia,-122.6604,Canada\n" +
            "53.7970,1,England,-1.6630,United Kingdom\n" +
            "21.3069,0,(not set),-157.8583,United States\n" +
            "38.9822,5,Kansas,-94.6708,United States\n" +
            "64.1333,1,(not set),-21.9333,Iceland\n" +
            "41.6828,2,Illinois,-88.3515,United States\n" +
            "53.4326,1,England,-1.3635,United Kingdom\n" +
            "0.0000,1,Haiti,0.0000,Haiti\n" +
            "-14.2235,1,Bahia,-42.7803,Brazil\n" +
            "52.5168,1,Overijssel,6.0830,Netherlands\n" +
            "44.7893,1,Minnesota,-93.6018,United States\n" +
            "40.7608,6,Utah,-111.8910,United States\n" +
            "28.0222,0,Florida,-81.7329,United States\n" +
            "51.9167,2,South Holland,4.5000,Netherlands\n" +
            "38.1074,0,California,-122.5697,United States\n" +
            "52.1548,1,Lower Saxony,9.9580,Germany\n" +
            "43.0467,1,New York,-77.0953,United States\n" +
            "53.2307,1,England,-0.5406,United Kingdom\n" +
            "42.4154,1,Massachusetts,-71.1565,United States\n" +
            "51.9356,2,Lubusz Voivodeship,15.5062,Poland\n" +
            "45.2733,1,New Brunswick,-66.0633,Canada\n" +
            "40.1245,1,Illinois,-87.6300,United States\n" +
            "32.5232,1,Louisiana,-92.6379,United States\n" +
            "36.5092,1,Tennessee,-86.8850,United States\n" +
            "45.5260,2,Quebec,-73.3407,Canada\n" +
            "-43.5321,3,Canterbury,172.6362,New Zealand\n" +
            "28.6000,2,Florida,-81.3392,United States\n" +
            "43.6532,72,Ontario,-79.3832,Canada\n" +
            "47.9030,1,Centre,1.9093,France\n" +
            "51.7527,1,England,-0.3394,United Kingdom\n" +
            "28.0581,0,Florida,-82.5153,United States\n" +
            "31.4638,1,Texas,-100.4370,United States\n" +
            "51.0524,1,(not set),4.8807,Belgium\n" +
            "40.8859,1,New Jersey,-74.0435,United States\n" +
            "51.4160,1,England,-0.7540,United Kingdom\n" +
            "0.0000,2,Florida,0.0000,United States\n" +
            "17.9833,1,(not set),-76.8000,Jamaica\n" +
            "26.4381,2,Florida,-81.8068,United States\n" +
            "47.9990,1,Baden-Wurttemberg,7.8421,Germany\n" +
            "0.0000,1,Nepal,0.0000,Nepal\n" +
            "33.1434,2,California,-117.1661,United States\n" +
            "50.7374,2,North Rhine-Westphalia,7.0982,Germany\n" +
            "44.9212,2,Minnesota,-93.4688,United States\n" +
            "-34.9286,7,South Australia,138.6000,Australia\n" +
            "33.1972,2,Texas,-96.6398,United States\n" +
            "58.6035,1,Kirov Oblast,49.6668,Russia\n" +
            "34.1975,1,California,-119.1770,United States\n" +
            "60.4509,1,(not set),22.2665,Finland\n" +
            "39.8367,2,Colorado,-105.0372,United States\n" +
            "-26.2041,2,Gauteng,28.0473,South Africa\n" +
            "41.3573,1,Illinois,-88.4212,United States\n" +
            "44.2520,1,Michigan,-85.4012,United States\n" +
            "44.5982,1,Emilia-Romagna,10.6909,Italy\n" +
            "30.5083,0,Texas,-97.6789,United States\n" +
            "46.6874,1,Canton of Bern,7.8696,Switzerland\n" +
            "41.0072,1,Iowa,-91.9658,United States\n" +
            "52.6784,1,England,-2.4453,United Kingdom\n" +
            "51.2917,0,Alberta,-114.0144,Canada\n" +
            "34.1533,1,California,-118.7617,United States\n" +
            "31.3183,1,Khuzestan,48.6706,Iran\n" +
            "0.0000,1,Scotland,0.0000,United Kingdom\n" +
            "33.6470,2,California,-117.6892,United States\n" +
            "43.4675,3,Ontario,-79.6877,Canada\n" +
            "10.9642,1,Atlantico,-74.7970,Colombia\n" +
            "21.5433,3,Makkah Province,39.1728,Saudi Arabia\n" +
            "35.6961,1,Tehran,51.4231,Iran\n" +
            "54.3520,2,Pomeranian Voivodeship,18.6466,Poland\n" +
            "52.9668,1,Oryol Oblast,36.0625,Russia\n" +
            "52.1129,0,Wales,-4.0785,United Kingdom\n" +
            "26.5318,1,Florida,-80.0905,United States\n" +
            "59.4370,1,(not set),24.7536,Estonia\n" +
            "35.5951,1,North Carolina,-82.5515,United States\n" +
            "33.8353,4,California,-117.9145,United States\n" +
            "35.6092,1,Tokyo,139.7302,Japan\n" +
            "43.2256,1,New York,-74.1721,United States\n" +
            "38.9339,1,Virginia,-77.1773,United States\n" +
            "28.5355,8,Uttar Pradesh,77.3910,India\n" +
            "-18.9146,1,Minas Gerais,-48.2754,Brazil\n" +
            "42.5047,4,Massachusetts,-71.1956,United States\n" +
            "6.2359,1,Antioquia,-75.5751,Colombia";
}
