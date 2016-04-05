// TODO: create 1000 users. Create 100 user groups and add 10 users to each groups.
// What about teams? + How to create / define roles ?
// The role/team is only allowed to access a small number of the created documents.
// Use the dummy script to create 1100 documents, 3 MB each.
// We should use this LPP when writing the uploading code:
// https://issues.liferay.com/browse/LPP-19999
// Contents of the dummy.txt:
// echo "This is just a sample line appended to create a big testfile" > dummy.txt
// for /L %%i in (1,1,5) do type dummy.txt >> dummy.txt
// for /L %%i in (1,1,10) do copy dummy.txt dummy%%i.txt

package com.test;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserGroupLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

/**
 * Portlet implementation class Uploader
 */
public class Uploader extends MVCPortlet {

	public void javaActionMethod(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException,
			PortletException, SystemException, PortalException {
		System.out.println("Testing starts..");

		String buttonValue = ParamUtil.get(actionRequest, "button1", "");
		System.out.println("The following button was pressed: " + buttonValue);

		if (buttonValue.equalsIgnoreCase("UploadFiles")) {

			// ThemeDisplay themeDisplay =
			// (ThemeDisplay)request.getAttribute(WebKeys.THEME_DISPLAY);
			// SessionMessages.add(request, PortalUtil.getPortletId(request) +
			// SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
			// long userId = themeDisplay.getUserId();
			// long groupId = themeDisplay.getScopeGroupId();
			//
			// UploadPortletRequest uploadRequest =
			// PortalUtil.getUploadPortletRequest(request);
			//
			// long folderId = 0;
			//
			// java.io.File submissionFile = null;
			// String submissionFileName = "";
			// try {
			// try {
			// ServiceContext serviceContext =
			// ServiceContextFactory.getInstance(DLFileEntry.class.getName(),
			// request);
			// } catch (SystemException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// } catch (PortalException e3) {
			// e3.printStackTrace();
			// }
			//
			// String contentType = "";
			// String changeLog = "";
			// Map<String, Fields> fieldsMap = new HashMap<String, Fields>();
			// long size = 0;
			//
			// if (uploadRequest != null) {
			//
			// submissionFileName =
			// uploadRequest.getFileName("uploadFileForMessage");
			// submissionFile = uploadRequest.getFile("uploadFileForMessage");
			// contentType =
			// uploadRequest.getContentType("uploadFileForMessage");
			// changeLog = ParamUtil.getString(request, "changeLog");
			// size = uploadRequest.getSize("uploadFileForMessage");
			//
			// }
			//
			// if(!submissionFileName.equals("")){
			//
			// ServiceContext serviceContext = null;
			// try {
			// try {
			// serviceContext =
			// ServiceContextFactory.getInstance(DLFileEntry.class.getName(),
			// request);
			// } catch (SystemException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// } catch (PortalException e3) {
			// e3.printStackTrace();
			// }
			//
			// String title = submissionFileName;
			// String description = "Message Media";
			// DLFileEntry dlf = null;
			// long realUserId = themeDisplay.getRealUserId();
			//
			// FileInputStream fis = null;
			//
			// try {
			// fis = new FileInputStream(submissionFile);
			// } catch (FileNotFoundException e1) {
			// e1.printStackTrace();
			// }
			//
			// try {
			//
			// dlf = DLFileEntryLocalServiceUtil.addFileEntry(realUserId,
			// groupId, groupId, 0, submissionFileName, contentType, title,
			// description, changeLog, 0, fieldsMap, submissionFile, fis, size,
			// serviceContext);
			// long feID = dlf.getFileEntryId();
			// DLFileEntryLocalServiceUtil.updateFileEntry(realUserId, feID,
			// submissionFileName, contentType, title, description, changeLog,
			// false, 0, fieldsMap, submissionFile, fis, size, serviceContext);
			//
			// String primKeyDLFE = dlf.getPrimaryKey() + "";
			// String[] actionIds = new String[1];
			//
			// actionIds[0] = "VIEW";
			//
			// Role user = RoleLocalServiceUtil.getRole(dlf.getCompanyId(),
			// RoleConstants.USER);
			// ResourcePermissionLocalServiceUtil.setResourcePermissions(dlf.getCompanyId(),
			// "com.liferay.portlet.documentlibrary.model.DLFileEntry", 4,
			// primKeyDLFE, user.getRoleId(), actionIds);
			//
			// } catch (DuplicateFileException e) {
			//
			// int randomNumber = randInt(1000, 9999);
			//
			// title = title + "_" + randomNumber;
			//
			// try {
			// try {
			// dlf = DLFileEntryLocalServiceUtil.addFileEntry(realUserId,
			// groupId, groupId, 0, submissionFileName, contentType, title,
			// description, changeLog, 0, fieldsMap, submissionFile, fis, size,
			// serviceContext);
			// } catch (SystemException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
			// } catch (PortalException e1) {
			// e1.printStackTrace();
			// }
			//
			// long feID = dlf.getFileEntryId();
			//
			// try {
			// try {
			// DLFileEntryLocalServiceUtil.updateFileEntry(realUserId, feID,
			// submissionFileName, contentType, title, description, changeLog,
			// false, 0, fieldsMap, submissionFile, fis, size, serviceContext);
			// } catch (SystemException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
			// } catch (PortalException e1) {
			// e1.printStackTrace();
			// }
			//
			// String[] actionIds = new String[1];
			//
			// actionIds[0] = "VIEW";
			// String primKeyDLFE = dlf.getPrimaryKey() + "";
			//
			// Role user = null;
			//
			// try {
			// try {
			// user = RoleLocalServiceUtil.getRole(dlf.getCompanyId(),
			// RoleConstants.USER);
			// } catch (SystemException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
			// } catch (PortalException e1) {
			// e1.printStackTrace();
			// }
			//
			// try {
			// try {
			// ResourcePermissionLocalServiceUtil.setResourcePermissions(dlf.getCompanyId(),
			// "com.liferay.portlet.documentlibrary.model.DLFileEntry", 4,
			// primKeyDLFE, user.getRoleId(), actionIds);
			// } catch (SystemException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
			// } catch (PortalException e1) {
			// e1.printStackTrace();
			// }
			//
			// } catch (PortalException e) {
			// e.printStackTrace();
			// } catch (SystemException e) {
			// e.printStackTrace();
			// }
			//
			// titleURL = "/documents/" + groupId + "/" + dlf.getFolderId() +
			// "/" + dlf.getTitle();
			//
			// }
			//
			// System.out.println("OPEN IMAGE FROM THIS PATH: " + titleURL);
			//
			// }
			//
			// public static int randInt(int min, int max) {
			//
			// Random rand = new Random();
			//
			// int randomNum = rand.nextInt((max - min) + 1) + min;
			//
			// return randomNum;
			//
			// }

		}
		if (buttonValue.equalsIgnoreCase("DeleteFiles")) {

			int count = DLFileEntryLocalServiceUtil.getFileEntriesCount();
			System.out.println("there are " + count + " files to be deleted");
			List<DLFileEntry> dlFileEntries = DLFileEntryLocalServiceUtil
					.getFileEntries(0, count);

			for (DLFileEntry dlFileEntry : dlFileEntries) {
				DLFileEntryLocalServiceUtil.deleteFileEntry(dlFileEntry
						.getFileEntryId());
			}
		}

		if (buttonValue.equalsIgnoreCase("CreateRoles")) {

			long userId;
			long companyId = 0;
			String name;
			String description;
			ServiceContext serviceContext = null;

			try {
				try {

					final Company company = CompanyLocalServiceUtil
							.getCompanies().iterator().next();
					companyId = company.getCompanyId();

					// long num = System.currentTimeMillis();

					for (int i = 1; i <= 3; i++) {
						try {
							UserLocalServiceUtil.addUser(
									PortalUtil.getUserId(actionRequest), // creatorUserId,
									companyId, // companyId,
									false, // autoPassword,
									"test", // password1,
									"test", // password2,
									true, // autoScreenName,
									null, // screenName,
									"test" + i + "@liferay.com", // emailAddress,
									0L, // facebookId,
									null, // openId,
									Locale.ENGLISH, // locale,
									"Test", // firstName,
									null, // middleName,
									"User" + i, // lastName,
									0, // prefixId,
									0, // suffixId,
									true, // male,
									1, // birthdayMonth,
									1, // birthdayDay,
									1977, // birthdayYear,
									null, // jobTitle,
									null, // groupIds,
									null, // organizationIds,
									null, // roleIds,
									null, // userGroupIds,
									false, // sendEmail,
									null);
							System.out.println("The user: User" + i
									+ " has been created");

						} catch (Exception e) {
							System.out.println("exception" + e);

							e.printStackTrace();
						}

					}

				} catch (SystemException e) {

					e.printStackTrace();
				}
			} finally {
				try {
					System.out.println("User count after: "
							+ UserLocalServiceUtil.getUsersCount());
				} catch (SystemException e) {

					e.printStackTrace();
				}
			}

			for (int j = 1; j <= 3; j++) {
				// int j = 1;

				User myUser = UserLocalServiceUtil.getUserByEmailAddress(
						companyId, "test" + j + "@liferay.com");
				UserGroup myUserGroup = UserGroupLocalServiceUtil.addUserGroup(
						myUser.getUserId(), companyId, "myUserGroup" + j,
						"description", serviceContext);
				System.out.println("The userGroup: myUserGroup" + j
						+ " has been created");
				UserGroupLocalServiceUtil.addUserUserGroup(myUser.getUserId(), myUserGroup);
				System.out.println("The user: " + myUser.getScreenName()
						+ " has been added to " + myUserGroup.getName());

			}

			// Object userGroups;
			// for (UserGroup userGroup : userGroups) {
			// String userGroupName = userGroup.getName();
			// // for locale specific title (optional, can be null)
			// Map<Locale, String> titleMap = new HashMap<Locale, String>();
			// titleMap.put(Locale.ENGLISH, userGroupName);
			//
			// // for locale specific description (optional, can be null)
			// Map<Locale, String> descriptionMap = new HashMap<Locale,
			// String>();
			// titleMap.put(Locale.ENGLISH, "Role created for UserGroup - " +
			// userGroupName);
			//
			// int type = RoleConstants.TYPE_REGULAR;
			//
			// // adding the role
			// Role role = RoleLocalServiceUtil.addRole(userId,
			// Role.class.getName(), 0,
			// userGroupName, titleMap, descriptionMap, type, null, null);
			//
			// // assigning the UserGroup to the role
			// GroupLocalServiceUtil.addRoleGroups(role.getRoleId(), new
			// long[]{userGroup.getGroupId()});
			// // need to pass groupId and not userGroupId
			// }
		}

		if (buttonValue.equalsIgnoreCase("DeleteRoles")) {

			// First, we delete all non-admin users:
			List<User> myUsers = UserLocalServiceUtil.getUsers(0, 99999);
			for (User user : myUsers) {
				if (user.isDefaultUser()
						|| PortalUtil.isOmniadmin(user.getUserId())) {
					System.out.println("Skipping user " + user.getScreenName());
				} else {
					User userToDelete = user;

					System.out.println("Deleting user "
							+ userToDelete.getScreenName());
					UserLocalServiceUtil.deleteUser(userToDelete);
				}
			}

			// Then, we delete userGroups:
			List<UserGroup> myUserGroups = UserGroupLocalServiceUtil
					.getUserGroups(0, 99999);
			for (UserGroup myUserGroup : myUserGroups) {

				System.out.println("Deleting userGroup "
						+ myUserGroup.getName());
				UserGroupLocalServiceUtil.deleteUserGroup(myUserGroup);
			}
		}

		System.out.println("Testing ends..");
	}

}
