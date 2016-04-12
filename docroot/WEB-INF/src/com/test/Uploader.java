// TODO: create 1000 users. Create 100 user groups and add 10 users to each groups.
// What about teams? + How to create / define roles ?
// The role/team is only allowed to access a small number of the created documents.
// Use the dummy script to create 1000 documents, 2 MB each.
// We should use this LPP when writing the uploading code:
// https://issues.liferay.com/browse/LPP-19999
// Contents of the dummy.bat:
// echo "This is just a sample line appended to create a big testfile" > dummy.txt
// for /L %%i in (1,1,14) do type dummy.txt >> dummy.txt
// for /L %%i in (1,1,10) do copy dummy.txt dummy%%i.txt
// It turned out we don't need userGroups, only teams/roles.
// Here is how to create teams:
// https://www.liferay.com/community/forums/-/message_boards/message/14883029

package com.test;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.upload.UploadServletRequest;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.ResourcePermission;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
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
import com.liferay.portlet.documentlibrary.service.DLAppLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil;
import com.liferay.portlet.dynamicdatamapping.storage.Fields;
import com.liferay.util.bridges.mvc.MVCPortlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

	public void javaActionMethod(ActionRequest actionRequest, ActionResponse actionResponse) throws IOException,
			PortletException, SystemException, PortalException {
		System.out.println("Testing starts..");

		String buttonValue = ParamUtil.get(actionRequest, "button1", "");
		System.out.println("The following button was pressed: " + buttonValue);

		String uploadCountString = ParamUtil.get(actionRequest, "uploadCount", "");
		int uploadCount = 0;
		if (!uploadCountString.equalsIgnoreCase("")) {
			System.out.println("upload is valid");
			uploadCount = Integer.parseInt(uploadCountString);

			for (int fileCount = 1; fileCount <= uploadCount; fileCount++) {
				File myFile = new File("..//..//a_files//dummy" + fileCount + ".txt");
				if (myFile.exists()) {
					ServiceContext serviceContext = new ServiceContext();
					serviceContext.setAddGroupPermissions(false);
					serviceContext.setAddGuestPermissions(false);
					serviceContext.setWorkflowAction(WorkflowConstants.ACTION_PUBLISH);

					DLFileEntry fileEntry = null;

					// DLAppLocalServiceUtil.addFileEntry(userId, repositoryId,
					// folderId, sourceFileName,
					// mimeType, title, description, changeLog, file,
					// serviceContext)

					long userId = 0;
					long groupId = 0;
					long repositoryId = 0;
					long folderId = 0;
					String sourceFileName = null;
					String mimeType = null;
					String title = null;
					String description = null;
					String changeLog = null;
					long fileEntryTypeId = 0;
					Map<String, Fields> fieldsMap = null;
					File file = null;
					InputStream is = null;
					long size = 0;
					long companyId;
					Group defGroup;
					final Company company = CompanyLocalServiceUtil.getCompanies().iterator().next();

					try {
						companyId = company.getCompanyId();
						userId = UserLocalServiceUtil.getUserIdByEmailAddress(companyId, "test@liferay.com");
						defGroup = GroupLocalServiceUtil.getGroup(companyId, "Guest");
						groupId = defGroup.getGroupId();
						repositoryId = groupId;
						sourceFileName = myFile.getName();
						mimeType = MimeTypesUtil.getContentType(myFile);
						title = sourceFileName;
						description = "description";
						changeLog = "";
						fileEntryTypeId = 0;
						fieldsMap = null;
						file = myFile;
						is = null;
						size = file.length();

					} catch (PortalException e1) {
						e1.printStackTrace();
					} catch (SystemException e1) {
						e1.printStackTrace();
					}

					try {
						DLAppLocalServiceUtil.addFileEntry(userId, repositoryId, folderId, sourceFileName, mimeType,
								title, description, changeLog, file, serviceContext);

					} catch (PortalException e) {
						e.printStackTrace();
					} catch (SystemException e) {
						e.printStackTrace();
					}
					System.out.println("This file was added: " + title);
				}
			}
		}

		if (buttonValue.equalsIgnoreCase("DeleteFiles")) {

			int count = DLFileEntryLocalServiceUtil.getFileEntriesCount();
			System.out.println("there are " + count + " files to be deleted");
			List<DLFileEntry> dlFileEntries = DLFileEntryLocalServiceUtil.getFileEntries(0, count);

			for (DLFileEntry dlFileEntry : dlFileEntries) {
				DLFileEntryLocalServiceUtil.deleteFileEntry(dlFileEntry.getFileEntryId());
			}
		}

		String userCountString = ParamUtil.get(actionRequest, "userCount", "");
		int userCount = 0;
		if (!userCountString.equalsIgnoreCase("")) {
			System.out.println("userCount is valid");
			userCount = Integer.parseInt(userCountString);

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

					for (int i = 1; i <= userCount; i++) {
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

			for (int j = 1; j <= userCount; j++) {
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

				// note that the default site is called "Guest" and not
				// "Liferay"
				Group defGroup = GroupLocalServiceUtil.getGroup(companyId, "Guest");
				long defGroupId = defGroup.getGroupId();
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
			int teamCount = TeamLocalServiceUtil.getTeamsCount();
			List<Team> myTeams = TeamLocalServiceUtil.getTeams(0, teamCount);
			for (Team myTeam : myTeams) {

				System.out.println("Deleting team " + myTeam.getName());
				TeamLocalServiceUtil.deleteTeam(myTeam);
			}
		}

		if (buttonValue.equalsIgnoreCase("ChangePermissions")) {

			int DLFileCount = DLFileEntryLocalServiceUtil.getFileEntriesCount();
			System.out.println("there are " + DLFileCount + " files on the server");
			List<DLFileEntry> dlFileEntries = DLFileEntryLocalServiceUtil.getFileEntries(0, DLFileCount);
			int fileEntryCounter = 1;
			int teamCounter = 1;
			int teamCount = TeamLocalServiceUtil.getTeamsCount();
			List<Team> myTeams = TeamLocalServiceUtil.getTeams(0, teamCount);
			Team myTestTeam;
			int scope;
			String primKey;

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
				Group defaultGroup = GroupLocalServiceUtil.getGroup(companyId, "Guest");

				System.out.println("filename = " + name);

				String[] actionIds = new String[1];
				actionIds[0] = "VIEW";

				scope = ResourceConstants.SCOPE_INDIVIDUAL;
				primKey = dlFileEntry.getPrimaryKey() + "";

				if (ResourcePermissionLocalServiceUtil.hasResourcePermission(companyId, resourceName,
						ResourceConstants.SCOPE_INDIVIDUAL, fileEntryId, siteMemberRole.getRoleId(), ActionKeys.VIEW)) {
					System.out.println("true, site member has permissions");
				}

				myTestTeam = TeamLocalServiceUtil.getTeam(defaultGroup.getGroupId(), "team" + teamCounter);
				// System.out.println("teamrole ID: " +
				// myTestTeam.getRole().getRoleId());

				ResourcePermission permission = null;
				// Removing Guest view permissions, if any
				if (ResourcePermissionLocalServiceUtil.hasResourcePermission(companyId, resourceName,
						ResourceConstants.SCOPE_INDIVIDUAL, fileEntryId, guestRole.getRoleId(), ActionKeys.VIEW)) {
					permission = ResourcePermissionLocalServiceUtil.getResourcePermission(companyId, resourceName,
							ResourceConstants.SCOPE_INDIVIDUAL, fileEntryId, guestRole.getRoleId());
					permission.setActionIds(0);
					ResourcePermissionLocalServiceUtil.updateResourcePermission(permission);
				}

				// Removing SiteMember view permissions, if any
				if (ResourcePermissionLocalServiceUtil.hasResourcePermission(companyId, resourceName,
						ResourceConstants.SCOPE_INDIVIDUAL, fileEntryId, siteMemberRole.getRoleId(), ActionKeys.VIEW)) {
					permission = ResourcePermissionLocalServiceUtil.getResourcePermission(companyId, resourceName,
							ResourceConstants.SCOPE_INDIVIDUAL, fileEntryId, siteMemberRole.getRoleId());
					permission.setActionIds(0);
					ResourcePermissionLocalServiceUtil.updateResourcePermission(permission);
				}

				// Adding Team view permissions
				if (ResourcePermissionLocalServiceUtil.hasResourcePermission(companyId, resourceName,
						ResourceConstants.SCOPE_INDIVIDUAL, fileEntryId, myTestTeam.getRole().getRoleId(),
						ActionKeys.VIEW)) {
					System.out.println("the team3 has permissions");
				} else {
					ResourcePermissionLocalServiceUtil.setResourcePermissions(companyId, resourceName, scope, primKey,
							myTestTeam.getRole().getRoleId(), actionIds);
				}

				fileEntryCounter++;
				teamCounter++;
				if (teamCounter > teamCount) {
					teamCounter = 1;
				}
			}
		}

		System.out.println("Testing ends..");
	}
}
