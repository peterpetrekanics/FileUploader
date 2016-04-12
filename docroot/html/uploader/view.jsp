<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<portlet:defineObjects />

Welcome to the <b>Uploader</b> portlet.

<portlet:actionURL var="buttonActionURL" windowState="normal"
name="javaActionMethod">
</portlet:actionURL>

<br>
<br>
<b>Instructions</b>
<br>
1. Create a new folder called: a_files in your Liferay home (the same place where data and deploy folders are)
<br>
<br>
2. Create files in the a_files folder, naming should be: dummy1.txt to dummy100.txt
<br>
<br>
3. Submit how many files you would like to have uploaded
<br>
<br>
4. Submit how many users you would like to have created
<br>
<br>
5. Finally, press the ChangePermissions button to modify the file permissions
<br>
<br>
 <form action="<%=buttonActionURL%>" name="buttonPressed"  method="POST">
	<input type="submit" name="button1" id="buttonID2" value="DeleteFiles"/>
	<input type="submit" name="button1" id="buttonID4" value="DeleteRoles"/>
	<input type="submit" name="button1" id="buttonID5" value="ChangePermissions"/>
</form>

<form action="<%=buttonActionURL%>" method="post">
    How many files should be uploaded : <input type="text" name="uploadCount"><br>
    <input type="submit" value="Submit"> 
</form>
<form action="<%=buttonActionURL%>" method="post">
    How many users should be created : <input type="text" name="userCount"><br>
    <input type="submit" value="Submit"> 
</form>