# FileUploader
Creates+deletes users, groups, teams - and then modifies file permissions 

The portlet is now ready, it has following functionalities:
- it can upload any number of text files into the portal from a specified folder
- it can create any number of users, and it adds users to site teams (for every 10 user a new team will be created)
- it can modify the uploaded files' permissions. The logic is: team 1 has view permissions on file1-file10, after that comes team 2, he has view permissions on file11-20 and so on. If there are no more teams, team 1 gets the next files again.

I wrote instructions on the portlet's view.jsp to make it easier to use.

I have used this .bat file to create 1000 text files (2 MB each):

echo "This is just a sample line appended to create a big testfile" > dummy.txt

for /L %%i in (1,1,14) do type dummy.txt >> dummy.txt

for /L %%i in (1,1,1000) do copy dummy.txt dummy%%i.txt


Feel free to use the portlet for testing

---

In case of:

java.lang.IllegalStateException: this writer hit an OutOfMemoryError; cannot commit

raise xmx memory to 2048 in setenv.bat and add the following properties in your portal-ext.properties:

lucene.commit.batch.size=100
lucene.commit.time.interval=100
