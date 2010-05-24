<%@ page import="java.util.Date" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.TimeZone" %>
<%@ page import="java.security.MessageDigest" %>
<%@ page import="com.easyinsight.users.Account" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="java.security.NoSuchAlgorithmException" %>
<%@ page import="com.easyinsight.billing.BillingUtil" %>
<%@ page import="com.easyinsight.security.PasswordService" %>
<%@ page import="com.easyinsight.users.User" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Formatter" %>
<%@ page import="com.easyinsight.config.ConfigLoader" %>
<%--
  Created by IntelliJ IDEA.
  User: abaldwin
  Date: Jun 21, 2009
  Time: 11:36:48 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html xmlns="http://www.w3.org/1999/xhtml" style="width:100%"><!-- InstanceBegin template="/Templates/Base.dwt" codeOutsideHTMLIsLocked="false" -->
<%
    long accountID = 0;
    Account account = null;
    User user = null;
    Session s = Database.instance().createSession();
    try {
        if(request.getParameter("username") != null && request.getParameter("password") != null) {
            String encryptedPass = PasswordService.getInstance().encrypt(request.getParameter("password"));

            List results = s.createQuery("from User where userName = ? and password = ?").setString(0, request.getParameter("username")).setString(1, encryptedPass).list();
            if(results.size() != 1) {
                response.sendRedirect("login.jsp?error=true");
                return;
            }
            user =(User) results.get(0);
            account = user.getAccount();
            accountID = account.getAccountID();
            request.getSession().setAttribute("accountID", accountID);
            request.getSession().setAttribute("userID", user.getUserID());
        }
        if(account == null) {
            if(request.getSession().getAttribute("accountID")== null) {
                response.sendRedirect("login.jsp?error=true");
                return;
            }
            accountID = (Long) request.getSession().getAttribute("accountID");
            long userID = (Long) request.getSession().getAttribute("userID");
            user = (User) s.createQuery("from User where userID = ?").setLong(0, userID).list().get(0);
            account = (Account) s.createQuery("from Account where accountID = ?").setLong(0, accountID).list().get(0);
        }
    }
    finally {
        s.close();
    }
    if((account.getAccountType() == Account.PROFESSIONAL || account.getAccountType() == Account.PREMIUM || account.getAccountType() == Account.ENTERPRISE)
            && !user.isAccountAdmin())
      response.sendRedirect("access.jsp");

    String keyID = BillingUtil.getKeyID();
    String key = BillingUtil.getKey();
    String orderID = "";
    String amount = "1.00";
    String type = "auth";
    if(account.getAccountState() == Account.DELINQUENT) {
        amount = String.valueOf(account.monthlyCharge());
        type = "sale";
    }

      DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
      df.setTimeZone(TimeZone.getTimeZone("UTC"));
      String time = df.format(new Date());
      request.getSession().setAttribute("billingTime", time);
      String hashString = orderID + "|" + amount + "|" + String.valueOf(accountID) + "|" + time + "|" + key;
      String hash = BillingUtil.MD5Hash(hashString);
      String accountInfoString = null;
      switch(account.getAccountType()) {
          case Account.BASIC:
              accountInfoString = "Basic";
              break;
          case Account.PROFESSIONAL:
              accountInfoString = "Professional";
              break;
          default:
              accountInfoString = "";
      }
      Formatter f = new Formatter();
      String charge = f.format("%.2f", account.monthlyCharge()).toString();

%>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Easy Insight - Billing</title>
<!-- InstanceEndEditable -->
<!-- InstanceBeginEditable name="head" -->
        <script language="javascript" type="text/javascript">
        function setCCexp() {
            document.getElementById("ccexp").value = document.getElementById("ccexpMonth").value + document.getElementById("ccexpYear").value;

        }
    </script>
    <style type="text/css">
        #centerPage p {
            text-indent:0px;
        }
        td:first-child {
            text-align:right;
        }
        span.error {
            color: red;
        }

        .submitButton {
            height:25px;
            width:66px;
            background-image: url('submit.gif');
        }

        .submitButton:hover{
            background-image: url('submitSelected.gif');
        }

        .submitButton:active{
            background-image: url('submitActive.gif');
        }

    </style>
<!-- InstanceEndEditable -->
    <link type="text/css" rel="stylesheet" media="screen" href="/css/base.css" />
</head>
<body style="width:100%;text-align:center;margin:0px auto;">
    <div style="width:1000px;border-left-style:solid;border-left-color:#DDDDDD;border-left-width:1px;border-right-style:solid;border-right-color:#DDDDDD;border-right-width:1px;margin:0 auto;">
    	<div style="width:100%;text-align:left;height:70px;position:relative">
        	<a href="/index.html"><img src="/images/logo.jpg" alt="Easy Insight Logo" name="logo" id="logo" /></a>
            <div class="signupHeadline"><a href="https://www.easy-insight.com/app/" class="signupButton"></a> <a href="https://www.easy-insight.com/app/#page=account" class="signupforfreeButton"></a></div>
            <div class="headline"><a id="productPage" href="/product.html">PRODUCT</a> <a id="dataPage" href="/data.html">DATA</a> <a id="solutionsPage" href="/webanalytics.html">SOLUTIONS</a> <a id="blogPage" href="http://jamesboe.blogspot.com/">BLOG</a>  <a id="companyPage" href="/company.html">COMPANY</a></div>
        </div>
	    <!-- InstanceBeginEditable name="submenu" -->
    	<!-- InstanceEndEditable -->
        <div id="content">
		    <!-- InstanceBeginEditable name="content" -->
            <div style="width:100%;background-color:#FFFFFF">
                <div style="width:100%;text-align:center"><h1 style="color:#FFFFFF;background-image:url(/images/banner-wide.jpg);background-repeat:repeat-y;padding:10px;">Billing</h1></div>
                <p>Please input your billing information below. Your first billing cycle will start upon completion of any remaining trial time. Easy Insight does not offer any type of refund after billing.</p>
        <p style="font-size:10px">Items marked with a <span class="error">*</span> are required.</p>
        <% if(request.getParameter("error") != null) { %>
            <p><span class="error">There was an error with your billing information. Please input the correct information below.</span></p>
        <% } %>
  <form method="post" action="https://secure.braintreepaymentgateway.com/api/transact.php" onsubmit="setCCexp()">
      <input id="ccexp" type="hidden" value="" name="ccexp"/>
      <input id="customer_vault_id" type="hidden" value="<%= accountID %>" name="customer_vault_id" />
      <input id="customer_vault" type="hidden" value="<%= (account.isBillingInformationGiven() != null && account.isBillingInformationGiven()) ? "update_customer" : "add_customer" %>" name="customer_vault" />
      <input id="redirect" type="hidden" value="<%= ConfigLoader.instance().getRedirectLocation() %>/app/billing/submit.jsp" name="redirect"/>
      <input id="payment" type="hidden" value="creditcard" name="creditcard" />
      <input id="key_id" type="hidden" value="<%= keyID %>" name="key_id"/>
      <input id="orderid" type="hidden" value="<%= orderID %>" name="orderid"/>
      <input id="amount" type="hidden" value="<%= amount %>" name="amount"/>
      <input id="time" type="hidden" value="<%= time %>" name="time"/>
      <input id="hash" type="hidden" value="<%= hash %>" name="hash"/>
      <input id="type" type="hidden" value="<%= type %>" name="type" />
      <table cellpadding="3"><tbody>
      <tr>
            <td>Company:</td><td><input id="company" type="text" value="" name="company" style="width:16.5em" /></td>
      </tr>
      <tr>
          <td>First Name:</td><td><input id="firstname" type="text" value="" name="firstname" style="width:16.5em" /><span class="error">*</span></td>
          <td>Last Name:</td><td colspan="3"><input id="lastname" type="text" value="" name="lastname" style="width:16.5em" /><span class="error">*</span></td>
      </tr>
      <tr>
          <td>Address 1:</td><td> <input id="address1" type="text" value="" name="address1" style="width:16.5em" /><span class="error">*</span></td>
      </tr>
      <tr>
          <td>Address 2:</td><td><input id="address2" type="text" value="" name="address2" style="width:16.5em" /></td>
      </tr>
      <tr>
          <td>City:</td><td><input id="city" type="text" value="" name="city" style="width:16.5em" /><span class="error">*</span></td><td style="text-align:right">State/Province:</td><td><input id="state" type="text" value="" style="width:2.5em" maxlength="2" name="state" /><span class="error">*</span></td><td style="text-align:right">Zip/Postal Code:</td><td> <input id="zip" type="text" value="" name="zip" /><span class="error">*</span></td>
      </tr>
      <tr>
          <td>Country:</td><td colspan="5"><select id="country" type="text" name="country">
            <option value="AF">AFGHANISTAN</option>
            <option value="AX">ÅLAND ISLANDS</option>
            <option value="AL">ALBANIA</option>
            <option value="DZ">ALGERIA</option>
            <option value="AS">AMERICAN SAMOA</option>
            <option value="AD">ANDORRA</option>
            <option value="AO">ANGOLA</option>
            <option value="AI">ANGUILLA</option>
            <option value="AQ">ANTARCTICA</option>
            <option value="AG">ANTIGUA AND BARBUDA</option>
            <option value="AR">ARGENTINA</option>
            <option value="AM">ARMENIA</option>
            <option value="AW">ARUBA</option>
            <option value="AU">AUSTRALIA</option>
            <option value="AT">AUSTRIA</option>
            <option value="AZ">AZERBAIJAN</option>
            <option value="BS">BAHAMAS</option>
            <option value="BH">BAHRAIN</option>
            <option value="BD">BANGLADESH</option>
            <option value="BB">BARBADOS</option>
            <option value="BY">BELARUS</option>
            <option value="BE">BELGIUM</option>
            <option value="BZ">BELIZE</option>
            <option value="BJ">BENIN</option>
            <option value="BM">BERMUDA</option>
            <option value="BT">BHUTAN</option>
            <option value="BO">BOLIVIA</option>
            <option value="BA">BOSNIA AND HERZEGOVINA</option>
            <option value="BW">BOTSWANA</option>
            <option value="BV">BOUVET ISLAND</option>
            <option value="BR">BRAZIL</option>
            <option value="IO">BRITISH INDIAN OCEAN TERRITORY</option>
            <option value="BN">BRUNEI DARUSSALAM</option>
            <option value="BG">BULGARIA</option>
            <option value="BF">BURKINA FASO</option>
            <option value="BI">BURUNDI</option>
            <option value="KH">CAMBODIA</option>
            <option value="CM">CAMEROON</option>
            <option value="CA">CANADA</option>
            <option value="CV">CAPE VERDE</option>
            <option value="KY">CAYMAN ISLANDS</option>
            <option value="CF">CENTRAL AFRICAN REPUBLIC</option>
            <option value="TD">CHAD</option>
            <option value="CL">CHILE</option>
            <option value="CN">CHINA</option>
            <option value="CX">CHRISTMAS ISLAND</option>
            <option value="CC">COCOS (KEELING) ISLANDS</option>
            <option value="CO">COLOMBIA</option>
            <option value="KM">COMOROS</option>
            <option value="CG">CONGO</option>
            <option value="CD">CONGO, THE DEMOCRATIC REPUBLIC OF THE</option>
            <option value="CK">COOK ISLANDS</option>
            <option value="CR">COSTA RICA</option>
            <option value="CI">COTE D'IVOIRE</option>
            <option value="HR">CROATIA</option>
            <option value="CU">CUBA</option>
            <option value="CY">CYPRUS</option>
            <option value="CZ">CZECH REPUBLIC</option>
            <option value="DK">DENMARK</option>
            <option value="DJ">DJIBOUTI</option>
            <option value="DM">DOMINICA</option>
            <option value="DO">DOMINICAN REPUBLIC</option>
            <option value="EC">ECUADOR</option>
            <option value="EG">EGYPT</option>
            <option value="SV">EL SALVADOR</option>
            <option value="GQ">EQUATORIAL GUINEA</option>
            <option value="ER">ERITREA</option>
            <option value="EE">ESTONIA</option>
            <option value="ET">ETHIOPIA</option>
            <option value="FK">FALKLAND ISLANDS (MALVINAS)</option>
            <option value="FO">FAROE ISLANDS</option>
            <option value="FJ">FIJI</option>
            <option value="FI">FINLAND</option>
            <option value="FR">FRANCE</option>
            <option value="GF">FRENCH GUIANA</option>
            <option value="PF">FRENCH POLYNESIA</option>
            <option value="TF">FRENCH SOUTHERN TERRITORIES</option>
            <option value="GA">GABON</option>
            <option value="GM">GAMBIA</option>
            <option value="GE">GEORGIA</option>
            <option value="DE">GERMANY</option>
            <option value="GH">GHANA</option>
            <option value="GI">GIBRALTAR</option>
            <option value="GR">GREECE</option>
            <option value="GL">GREENLAND</option>
            <option value="GD">GRENADA</option>
            <option value="GP">GUADELOUPE</option>
            <option value="GU">GUAM</option>
            <option value="GT">GUATEMALA</option>
            <option value="GG">GUERNSEY</option>
            <option value="GN">GUINEA</option>
            <option value="GW">GUINEA-BISSAU</option>
            <option value="GY">GUYANA</option>
            <option value="HT">HAITI</option>
            <option value="HM">HEARD ISLAND AND MCDONALD ISLANDS</option>
            <option value="VA">HOLY SEE (VATICAN CITY STATE)</option>
            <option value="HN">HONDURAS</option>
            <option value="HK">HONG KONG</option>
            <option value="HU">HUNGARY</option>
            <option value="IS">ICELAND</option>
            <option value="IN">INDIA</option>
            <option value="ID">INDONESIA</option>
            <option value="IR">IRAN, ISLAMIC REPUBLIC OF</option>
            <option value="IQ">IRAQ</option>
            <option value="IE">IRELAND</option>
            <option value="IM">ISLE OF MAN</option>
            <option value="IL">ISRAEL</option>
            <option value="IT">ITALY</option>
            <option value="JM">JAMAICA</option>
            <option value="JP">JAPAN</option>
            <option value="JE">JERSEY</option>
            <option value="JO">JORDAN</option>
            <option value="KZ">KAZAKHSTAN</option>
            <option value="KE">KENYA</option>
            <option value="KI">KIRIBATI</option>
            <option value="KP">KOREA, DEMOCRATIC PEOPLE'S REPUBLIC OF</option>
            <option value="KR">KOREA, REPUBLIC OF</option>
            <option value="KW">KUWAIT</option>
            <option value="KG">KYRGYZSTAN</option>
            <option value="LA">LAO PEOPLE'S DEMOCRATIC REPUBLIC</option>
            <option value="LV">LATVIA</option>
            <option value="LB">LEBANON</option>
            <option value="LS">LESOTHO</option>
            <option value="LR">LIBERIA</option>
            <option value="LY">LIBYAN ARAB JAMAHIRIYA</option>
            <option value="LI">LIECHTENSTEIN</option>
            <option value="LT">LITHUANIA</option>
            <option value="LU">LUXEMBOURG</option>
            <option value="MO">MACAO</option>
            <option value="MK">MACEDONIA, THE FORMER YUGOSLAV REPUBLIC OF</option>
            <option value="MG">MADAGASCAR</option>
            <option value="MW">MALAWI</option>
            <option value="MY">MALAYSIA</option>
            <option value="MV">MALDIVES</option>
            <option value="ML">MALI</option>
            <option value="MT">MALTA</option>
            <option value="MH">MARSHALL ISLANDS</option>
            <option value="MQ">MARTINIQUE</option>
            <option value="MR">MAURITANIA</option>
            <option value="MU">MAURITIUS</option>
            <option value="YT">MAYOTTE</option>
            <option value="MX">MEXICO</option>
            <option value="FM">MICRONESIA, FEDERATED STATES OF</option>
            <option value="MD">MOLDOVA, REPUBLIC OF</option>
            <option value="MC">MONACO</option>
            <option value="MN">MONGOLIA</option>
            <option value="ME">MONTENEGRO</option>
            <option value="MS">MONTSERRAT</option>
            <option value="MA">MOROCCO</option>
            <option value="MZ">MOZAMBIQUE</option>
            <option value="MM">MYANMAR</option>
            <option value="NA">NAMIBIA</option>
            <option value="NR">NAURU</option>
            <option value="NP">NEPAL</option>
            <option value="NL">NETHERLANDS</option>
            <option value="AN">NETHERLANDS ANTILLES</option>
            <option value="NC">NEW CALEDONIA</option>
            <option value="NZ">NEW ZEALAND</option>
            <option value="NI">NICARAGUA</option>
            <option value="NE">NIGER</option>
            <option value="NG">NIGERIA</option>
            <option value="NU">NIUE</option>
            <option value="NF">NORFOLK ISLAND</option>
            <option value="MP">NORTHERN MARIANA ISLANDS</option>
            <option value="NO">NORWAY</option>
            <option value="OM">OMAN</option>
            <option value="PK">PAKISTAN</option>
            <option value="PW">PALAU</option>
            <option value="PS">PALESTINIAN TERRITORY, OCCUPIED</option>
            <option value="PA">PANAMA</option>
            <option value="PG">PAPUA NEW GUINEA</option>
            <option value="PY">PARAGUAY</option>
            <option value="PE">PERU</option>
            <option value="PH">PHILIPPINES</option>
            <option value="PN">PITCAIRN</option>
            <option value="PL">POLAND</option>
            <option value="PT">PORTUGAL</option>
            <option value="PR">PUERTO RICO</option>
            <option value="QA">QATAR</option>
            <option value="RE">REUNION</option>
            <option value="RO">ROMANIA</option>
            <option value="RU">RUSSIAN FEDERATION</option>
            <option value="RW">RWANDA</option>
            <option value="SH">SAINT HELENA</option>
            <option value="KN">SAINT KITTS AND NEVIS</option>
            <option value="LC">SAINT LUCIA</option>
            <option value="PM">SAINT PIERRE AND MIQUELON</option>
            <option value="VC">SAINT VINCENT AND THE GRENADINES</option>
            <option value="WS">SAMOA</option>
            <option value="SM">SAN MARINO</option>
            <option value="ST">SAO TOME AND PRINCIPE</option>
            <option value="SA">SAUDI ARABIA</option>
            <option value="SN">SENEGAL</option>
            <option value="RS">SERBIA</option>
            <option value="SC">SEYCHELLES</option>
            <option value="SL">SIERRA LEONE</option>
            <option value="SG">SINGAPORE</option>
            <option value="SK">SLOVAKIA</option>
            <option value="SI">SLOVENIA</option>
            <option value="SB">SOLOMON ISLANDS</option>
            <option value="SO">SOMALIA</option>
            <option value="ZA">SOUTH AFRICA</option>
            <option value="GS">SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS</option>
            <option value="ES">SPAIN</option>
            <option value="LK">SRI LANKA</option>
            <option value="SD">SUDAN</option>
            <option value="SR">SURINAME</option>
            <option value="SJ">SVALBARD AND JAN MAYEN</option>
            <option value="SZ">SWAZILAND</option>
            <option value="SE">SWEDEN</option>
            <option value="CH">SWITZERLAND</option>
            <option value="SY">SYRIAN ARAB REPUBLIC</option>
            <option value="TW">TAIWAN, PROVINCE OF CHINA</option>
            <option value="TJ">TAJIKISTAN</option>
            <option value="TZ">TANZANIA, UNITED REPUBLIC OF</option>
            <option value="TH">THAILAND</option>
            <option value="TL">TIMOR-LESTE</option>
            <option value="TG">TOGO</option>
            <option value="TK">TOKELAU</option>
            <option value="TO">TONGA</option>
            <option value="TT">TRINIDAD AND TOBAGO</option>
            <option value="TN">TUNISIA</option>
            <option value="TR">TURKEY</option>
            <option value="TM">TURKMENISTAN</option>
            <option value="TC">TURKS AND CAICOS ISLANDS</option>
            <option value="TV">TUVALU</option>
            <option value="UG">UGANDA</option>
            <option value="UA">UKRAINE</option>
            <option value="AE">UNITED ARAB EMIRATES</option>
            <option value="GB">UNITED KINGDOM</option>
            <option value="US" selected="selected">UNITED STATES</option>
            <option value="UM">UNITED STATES MINOR OUTLYING ISLANDS</option>
            <option value="UY">URUGUAY</option>
            <option value="UZ">UZBEKISTAN</option>
            <option value="VU">VANUATU</option>
            <option value="VE">VENEZUELA</option>
            <option value="VN">VIET NAM</option>
            <option value="VG">VIRGIN ISLANDS, BRITISH</option>
            <option value="VI">VIRGIN ISLANDS, U.S.</option>
            <option value="WF">WALLIS AND FUTUNA</option>
            <option value="EH">WESTERN SAHARA</option>
            <option value="YE">YEMEN</option>
            <option value="ZM">ZAMBIA</option>
            <option value="ZW">ZIMBABWE</option>
          </select>
      </td>
      </tr>
      <tr>
          <td>Phone #:</td> <td><input id="phone" type="text" value="" name="phone" style="width:16.5em" /><span class="error">*</span></td>
      </tr>
      <tr>
          <td>Credit Card Number:</td><td><input id="ccnumber" type="text" style="width:16.5em" name="ccnumber"/><span class="error">*</span></td><td>CVV/CVC:</td> <td><input id="cvv" type="text" value="" name="cvv" style="width:3.5em" /><span class="error">*</span></td>
      </tr>
      <tr><td>Expiration date:</td><td><select id="ccexpMonth">
          <option value="01">01 - January</option>
          <option value="02">02 - February</option>
          <option value="03">03 - March</option>
          <option value="04">04 - April</option>
          <option value="05">05 - May</option>
          <option value="06">06 - June</option>
          <option value="07">07 - July</option>
          <option value="08">08 - August</option>
          <option value="09">09 - September</option>
          <option value="10">10 - October</option>
          <option value="11">11 - November</option>
          <option value="12">12 - December</option>
      </select> /
      <select id="ccexpYear">
          <option value="10">10</option>
          <option value="11">11</option>
          <option value="12">12</option>
          <option value="13">13</option>
          <option value="14">14</option>
          <option value="15">15</option>
          <option value="16">16</option>
          <option value="17">17</option>
          <option value="18">18</option>
          <option value="19">19</option>
          <option value="20">20</option>
      </select>
      <span class="error">*</span></td></tr>
      <tr><td colspan="6" style="font-size:14px;text-align:left">You are signing up for the <%= accountInfoString %> account tier, and you will be charged $<%= charge %> USD monthly for your subscription.  <% if(account.getAccountState() == Account.DELINQUENT) { %> Since your account is currently delinquent, the first charge will be applied today. <% } %>  </td></tr>
      <tr><td colspan="6" style="text-align:center"> <input class="submitButton" type="image" value="" src="transparent.gif" name="commit"/></td></tr>
      </tbody></table>


  </form>
            </div>
    		<!-- InstanceEndEditable -->
<div id="footer" style="margin:0px;padding:12px 0px;width:100%;text-align:left">
            	<div style="float:right;padding-right:200px;">
                    <span style="font-weight:bold;font-size:12px">Security and Privacy</span>
                    <ul>
                        <li><a href="/terms.html">Terms of Service</a></li>
                        <li><a href="/privacy.html">Privacy Policy</a></li>
                    </ul>
                </div>
                <div style="padding-left:180px;">
					<span style="font-weight:bold;font-size:12px;">About</span>
                    <ul>
                        <li><a href="/company.html">Company Overview</a></li>
                        <li><a href="/whoweare.html">Who We Are</a></li>
                        <li><a href="/contactus.html">Contact Us</a></li>
                    </ul>
                </div>
            </div>
	    </div>
    </div>
</body>
<!-- InstanceEnd --></html>