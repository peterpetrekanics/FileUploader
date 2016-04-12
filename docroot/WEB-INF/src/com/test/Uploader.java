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
// It turned out we don't need userGroups, only teams/roles.
// Here is how to create teams:
// https://www.liferay.com/community/forums/-/message_boards/message/14883029

package com.test;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.ResourcePermission;
import com.liferay.portal.model.ResourcePermissionModel;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.RoleModel;
import com.liferay.portal.model.Team;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.TeamLocalServiceUtil;
import com.liferay.portal.service.UserGroupLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

/**
 * Portlet implementation class Uploader
 */
public class Uploader extends MVCPortlet {

	public void javaActionMethod(ActionRequest actionRequest, ActionResponse actionResponse) throws IOException,
			PortletException, SystemException, PortalException {
		System.out.println("Testing starts..");

		String buttonValue = ParamUtil.get(actionRequest, "button1", "");
		System.out.println("The following button was pressed: " + buttonValue);

		if (buttonValue.equalsIgnoreCase("UploadFiles")) {

			int count = DLFileEntryLocalServiceUtil.getFileEntriesCount();
			System.out.println("there are " + count + " files on the server");
			List<DLFileEntry> dlFileEntries = DLFileEntryLocalServiceUtil.getFileEntries(0, count);

			for (DLFileEntry dlFileEntry : dlFileEntries) {
				String name = dlFileEntry.getTitle();
				long feID = dlFileEntry.getFileEntryId();
				String resourceName = DLFileEntry.class.getName();
				String fileEntryId = feID + "";
				long companyId = dlFileEntry.getCompanyId();
				// Role user = RoleLocalServiceUtil.getRole(companyId,
				// RoleConstants.USER);
				Role siteMemberRole = RoleLocalServiceUtil.getRole(companyId, RoleConstants.SITE_MEMBER);
				Role guestRole = RoleLocalServiceUtil.getRole(companyId, RoleConstants.GUEST);

				if (name.equalsIgnoreCase("koal")) {
					System.out.println("filename = " + name);

					String[] actionIds = new String[1];
					actionIds[0] = "VIEW";

					// ResourcePermissionLocalServiceUtil.setResourcePermissions(dlFileEntry.getCompanyId(),
					// "com.liferay.portlet.documentlibrary.model.DLFileEntry",
					// 4,
					// primKeyDLFE, user.getRoleId(), actionIds);
					// System.out.println("milyen user: " + user);

					if (ResourcePermissionLocalServiceUtil.hasResourcePermission(companyId, resourceName,
							ResourceConstants.SCOPE_INDIVIDUAL, fileEntryId, siteMemberRole.getRoleId(),
							ActionKeys.VIEW)) {
						System.out.println("true, site member has permissions");
					}

					Group defaultGroup = GroupLocalServiceUtil.getGroup(companyId, "Guest");
					Team myTestTeam = TeamLocalServiceUtil.getTeam(defaultGroup.getGroupId(), "team3");
					// System.out.println("teamrole ID: " +
					// myTestTeam.getRole().getRoleId());

					if (ResourcePermissionLocalServiceUtil.hasResourcePermission(companyId, resourceName,
							ResourceConstants.SCOPE_INDIVIDUAL, fileEntryId, myTestTeam.getRole().getRoleId(),
							ActionKeys.VIEW)) {
						System.out.println("the team3 has permissions");

						ResourcePermission permission = null;
						if (ResourcePermissionLocalServiceUtil
								.hasResourcePermission(companyId, resourceName, ResourceConstants.SCOPE_INDIVIDUAL,
										fileEntryId, guestRole.getRoleId(), ActionKeys.VIEW)) {
							permission = ResourcePermissionLocalServiceUtil.getResourcePermission(companyId,
									resourceName, ResourceConstants.SCOPE_INDIVIDUAL, fileEntryId,
									guestRole.getRoleId());
							permission.setActionIds(0);
							ResourcePermissionLocalServiceUtil.updateResourcePermission(permission);
						}

						if (ResourcePermissionLocalServiceUtil.hasResourcePermission(companyId, resourceName,
								ResourceConstants.SCOPE_INDIVIDUAL, fileEntryId, siteMemberRole.getRoleId(),
								ActionKeys.VIEW)) {
							permission = ResourcePermissionLocalServiceUtil.getResourcePermission(companyId,
									resourceName, ResourceConstants.SCOPE_INDIVIDUAL, fileEntryId,
									siteMemberRole.getRoleId());
							permission.setActionIds(0);
							ResourcePermissionLocalServiceUtil.updateResourcePermission(permission);
						}
						
						System.out.println("and now guest and sitememb view p is gone");

					}

				}
			}

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
			List<DLFileEntry> dlFileEntries = DLFileEntryLocalServiceUtil.getFileEntries(0, count);

			for (DLFileEntry dlFileEntry : dlFileEntries) {
				DLFileEntryLocalServiceUtil.deleteFileEntry(dlFileEntry.getFileEntryId());
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

					final Company company = CompanyLocalServiceUtil.getCompanies().iterator().next();
					companyId = company.getCompanyId();

					// long num = System.currentTimeMillis();

					for (int i = 1; i <= 22; i++) {
						try {
							UserLocalServiceUtil.addUser(PortalUtil.getUserId(actionRequest), // creatorUserId,
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
							System.out.println("The user: User" + i + " has been created");

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
					System.out.println("User count after: " + UserLocalServiceUtil.getUsersCount());
				} catch (SystemException e) {

					e.printStackTrace();
				}
			}

			int teamCounter = 1;

			for (int j = 1; j <= 22; j++) {

				User myUser = UserLocalServiceUtil.getUserByEmailAddress(companyId, "test" + j + "@liferay.com");
				// UserGroup myUserGroup =
				// UserGroupLocalServiceUtil.addUserGroup(
				// myUser.getUserId(), companyId, "myUserGroup" + j,
				// "description", serviceContext);
				// System.out.println("The userGroup: myUserGroup" + j
				// + " has been created");
				// UserGroupLocalServiceUtil.addUserUserGroup(myUser.getUserId(),
				// myUserGroup);
				// System.out.println("The user: " + myUser.getScreenName()
				// + " has been added to " + myUserGroup.getName());
				//
				//
				Group defGroup = GroupLocalServiceUtil.getGroup(companyId, "Guest");
				long defGroupId = defGroup.getGroupId(); // note that the
															// default site is
															// called "Guest"
															// and not "Liferay"
				userId = myUser.getUserId();
				GroupLocalServiceUtil.addUserGroup(userId, defGroup);

				if (j == 1) {
					TeamLocalServiceUtil.addTeam(userId, defGroupId, "team1", "description");
					System.out.println("The team: team" + " has been created");

				} else if (j % 10 == 0) {
					teamCounter++;
					TeamLocalServiceUtil.addTeam(userId, defGroupId, "team" + teamCounter, "description");
					System.out.println("The team: team" + teamCounter + " has been created");

				}
				Team myTeam = TeamLocalServiceUtil.getTeam(defGroupId, "team" + teamCounter);
				System.out.println("myteam: " + myTeam.getName());
				TeamLocalServiceUtil.addUserTeam(userId, myTeam);

			}

			// -- commenting out userGroup creation - START
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
			// -- commenting out userGroup creation - END
		}

		if (buttonValue.equalsIgnoreCase("DeleteRoles")) {

			// First, we delete all non-admin users:
			List<User> myUsers = UserLocalServiceUtil.getUsers(0, 99999);
			for (User user : myUsers) {
				if (user.isDefaultUser() || PortalUtil.isOmniadmin(user.getUserId())) {
					System.out.println("Skipping user " + user.getScreenName());
				} else {
					User userToDelete = user;

					System.out.println("Deleting user " + userToDelete.getScreenName());
					UserLocalServiceUtil.deleteUser(userToDelete);
				}
			}

			// Then, we delete userGroups:
			List<UserGroup> myUserGroups = UserGroupLocalServiceUtil.getUserGroups(0, 99999);
			for (UserGroup myUserGroup : myUserGroups) {

				System.out.println("Deleting userGroup " + myUserGroup.getName());
				UserGroupLocalServiceUtil.deleteUserGroup(myUserGroup);
			}

			// Then, we delete teams:
			List<Team> myTeams = TeamLocalServiceUtil.getTeams(0, 99999);
			for (Team myTeam : myTeams) {

				System.out.println("Deleting team " + myTeam.getName());
				TeamLocalServiceUtil.deleteTeam(myTeam);
			}
		}

		System.out.println("Testing ends..");
	}

}
