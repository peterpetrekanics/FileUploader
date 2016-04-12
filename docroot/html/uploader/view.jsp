<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<portlet:defineObjects />

This is the <b>Uploader</b> portlet in View mode.

<portlet:actionURL var="buttonActionURL" windowState="normal"
name="javaActionMethod">
</portlet:actionURL>

 <form action="<%=buttonActionURL%>" name="buttonPressed"  method="POST">
	<input type="submit" name="button1" id="buttonID1" value="UploadFiles"/>
	<input type="submit" name="button1" id="buttonID2" value="DeleteFiles"/>
	<input type="submit" name="button1" id="buttonID3" value="CreateRoles"/>
	<input type="submit" name="button1" id="buttonID4" value="DeleteRoles"/>
	<input type="submit" name="button1" id="buttonID5" value="ChangePermissions"/>
</form>