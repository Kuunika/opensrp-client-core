package org.smartregister.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.BaseUnitTest;
import org.smartregister.CoreLibrary;
import org.smartregister.DristhiConfiguration;
import org.smartregister.SyncConfiguration;
import org.smartregister.domain.TimeStatus;
import org.smartregister.domain.jsonmapping.LoginResponseData;
import org.smartregister.domain.jsonmapping.Time;
import org.smartregister.domain.jsonmapping.User;
import org.smartregister.repository.AllSettings;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.Repository;
import org.smartregister.sync.SaveANMLocationTask;
import org.smartregister.sync.SaveANMTeamTask;
import org.smartregister.sync.SaveUserInfoTask;
import org.smartregister.util.AssetHandler;
import org.smartregister.util.Session;
import org.smartregister.view.activity.DrishtiApplication;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.TimeZone;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.smartregister.AllConstants.ENGLISH_LOCALE;
import static org.smartregister.AllConstants.KANNADA_LOCALE;

public class UserServiceTest extends BaseUnitTest {
    @Mock
    private Repository repository;
    @Mock
    private AllSettings allSettings;
    @Mock
    private AllSharedPreferences allSharedPreferences;
    @Mock
    private Session session;
    @Mock
    private HTTPAgent httpAgent;
    @Mock
    private DristhiConfiguration configuration;
    @Mock
    private SaveANMLocationTask saveANMLocationTask;
    @Mock
    private SaveUserInfoTask saveUserInfoTask;
    @Mock
    private SaveANMTeamTask saveANMTeamTask;

    private UserService userService;

    private String userInfoJSON;

    private LoginResponseData loginResponseData;

    @Mock
    private DrishtiApplication drishtiApplication;

    private byte[] password = "password Z".getBytes();

    @Before
    public void setUp() {
        initMocks(this);
        Whitebox.setInternalState(DrishtiApplication.getInstance(), "repository", repository);
        when(configuration.getDrishtiApplication()).thenReturn(drishtiApplication);
        Mockito.doReturn(repository).when(drishtiApplication).getRepository();
        userService = Mockito.spy(new UserService(allSettings, allSharedPreferences, httpAgent, session, configuration, saveANMLocationTask, saveUserInfoTask, saveANMTeamTask));
        userInfoJSON = "{\"locations\":{\"locationsHierarchy\":{\"map\":{\"cd4ed528-87cd-42ee-a175-5e7089521ebd\":{\"id\":\"cd4ed528-87cd-42ee-a175-5e7089521ebd\",\"label\":\"Pakistan\",\"node\":{\"locationId\":\"cd4ed528-87cd-42ee-a175-5e7089521ebd\",\"name\":\"Pakistan\",\"tags\":[\"Country\"],\"voided\":false},\"children\":{\"461f2be7-c95d-433c-b1d7-c68f272409d7\":{\"id\":\"461f2be7-c95d-433c-b1d7-c68f272409d7\",\"label\":\"Sindh\",\"node\":{\"locationId\":\"461f2be7-c95d-433c-b1d7-c68f272409d7\",\"name\":\"Sindh\",\"parentLocation\":{\"locationId\":\"cd4ed528-87cd-42ee-a175-5e7089521ebd\",\"name\":\"Pakistan\",\"voided\":false},\"tags\":[\"Province\"],\"voided\":false},\"children\":{\"a529e2fc-6f0d-4e60-a5df-789fe17cca48\":{\"id\":\"a529e2fc-6f0d-4e60-a5df-789fe17cca48\",\"label\":\"Karachi\",\"node\":{\"locationId\":\"a529e2fc-6f0d-4e60-a5df-789fe17cca48\",\"name\":\"Karachi\",\"parentLocation\":{\"locationId\":\"461f2be7-c95d-433c-b1d7-c68f272409d7\",\"name\":\"Sindh\",\"parentLocation\":{\"locationId\":\"cd4ed528-87cd-42ee-a175-5e7089521ebd\",\"name\":\"Pakistan\",\"voided\":false},\"voided\":false},\"tags\":[\"City\"],\"voided\":false},\"children\":{\"60c21502-fec1-40f5-b77d-6df3f92771ce\":{\"id\":\"60c21502-fec1-40f5-b77d-6df3f92771ce\",\"label\":\"Baldia\",\"node\":{\"locationId\":\"60c21502-fec1-40f5-b77d-6df3f92771ce\",\"name\":\"Baldia\",\"parentLocation\":{\"locationId\":\"a529e2fc-6f0d-4e60-a5df-789fe17cca48\",\"name\":\"Karachi\",\"parentLocation\":{\"locationId\":\"461f2be7-c95d-433c-b1d7-c68f272409d7\",\"name\":\"Sindh\",\"voided\":false},\"voided\":false},\"tags\":[\"Town\"],\"attributes\":{\"at1\":\"atttt1\"},\"voided\":false},\"parent\":\"a529e2fc-6f0d-4e60-a5df-789fe17cca48\"}},\"parent\":\"461f2be7-c95d-433c-b1d7-c68f272409d7\"}},\"parent\":\"cd4ed528-87cd-42ee-a175-5e7089521ebd\"}}}},\"parentChildren\":{\"cd4ed528-87cd-42ee-a175-5e7089521ebd\":[\"461f2be7-c95d-433c-b1d7-c68f272409d7\"],\"461f2be7-c95d-433c-b1d7-c68f272409d7\":[\"a529e2fc-6f0d-4e60-a5df-789fe17cca48\"],\"a529e2fc-6f0d-4e60-a5df-789fe17cca48\":[\"60c21502-fec1-40f5-b77d-6df3f92771ce\"]}}},\"user\":{\"username\":\"demotest\",\"roles\":[\"Provider\",\"Authenticated\",\"Thrive Member\"],\"permissions\":[\"Delete Reports\",\"Remove Allergies\",\"Edit Cohorts\",\"View Unpublished Forms\",\"Patient Dashboard - View Patient Summary\",\"Add Relationships\",\"Edit Problems\",\"Remove Problems\",\"Patient Dashboard - View Forms Section\",\"Manage Report Designs\",\"Add Patient Programs\",\"Delete Orders\",\"Manage Identifier Types\",\"Manage Person Attribute Types\",\"Add Patient Identifiers\",\"View Visit Types\",\"View Patients\",\"Delete Concept Proposals\",\"View Identifier Types\",\"Delete Encounters\",\"View Global Properties\",\"Edit Visits\",\"View Concept Map Types\",\"Add Users\",\"Delete Cohorts\",\"Manage Scheduled Report Tasks\",\"View Concepts\",\"Edit Concept Proposals\",\"Add Visits\",\"Edit Patient Programs\",\"Manage Concept Datatypes\",\"Manage Indicator Definitions\",\"View Concept Proposals\",\"Add Allergies\",\"Edit Allergies\",\"Delete Observations\",\"View Roles\",\"Manage Address Templates\",\"Configure Visits\",\"Manage Data Set Definitions\",\"View Concept Sources\",\"Patient Dashboard - View Regimen Section\",\"View Calculations\",\"Manage Encounter Roles\",\"Delete People\",\"Edit Report Objects\",\"View People\",\"Manage Concept Sources\",\"View Orders\",\"Manage Concept Map Types\",\"Delete Patient Programs\",\"Add Problems\",\"Edit People\",\"Manage Locations\",\"View Patient Programs\",\"View Field Types\",\"View Relationship Types\",\"Manage Visit Attribute Types\",\"Manage Order Types\",\"Manage TeamLocation Attribute Types\",\"Form Entry\",\"View Encounter Types\",\"View Encounter Roles\",\"Manage Programs\",\"Edit Reports\",\"View TeamLocation Attribute Types\",\"View Order Types\",\"Manage Relationship Types\",\"Manage Providers\",\"Manage Reports\",\"Manage Concept Classes\",\"Add Concept Proposals\",\"View Patient Identifiers\",\"View Privileges\",\"View Data Entry Statistics\",\"Patient Dashboard - View Graphs Section\",\"Manage Tokens\",\"Add Reports\",\"View Forms\",\"View Administration Functions\",\"Manage Relationships\",\"View Observations\",\"View Team\",\"Add Observations\",\"View Member\",\"View Report Objects\",\"Edit Relationships\",\"View Relationships\",\"Manage Scheduler\",\"View Allergies\",\"View Concept Reference Terms\",\"View Encounters\",\"Edit Patient Identifiers\",\"Edit Observations\",\"Delete Patients\",\"Delete Patient Identifiers\",\"View Person Attribute Types\",\"Add Encounters\",\"View Problems\",\"Manage FormEntry XSN\",\"View Visits\",\"Edit Team\",\"Manage Field Types\",\"Patient Dashboard - View Encounters Section\",\"Add Team\",\"Add Cohorts\",\"Add Patients\",\"Patient Dashboard - View Demographics Section\",\"Manage Concepts\",\"View Rule Definitions\",\"Add Orders\",\"Manage Visit Types\",\"Patient Dashboard - View Visits Section\",\"View Locations\",\"Manage Forms\",\"Edit Encounters\",\"Delete Relationships\",\"Manage Concept Reference Terms\",\"Add Report Objects\",\"Manage Alerts\",\"View Users\",\"Edit Patients\",\"Manage Concept Stop Words\",\"View Concept Classes\",\"View Patient Cohorts\",\"View Visit Attribute Types\",\"Manage TeamLocation Tags\",\"Manage Encounter Types\",\"View Concept Datatypes\",\"View Navigation Menu\",\"Delete Visits\",\"Add People\",\"Edit Orders\",\"Manage Concept Name tags\",\"Run Reports\",\"View Providers\",\"Patient Dashboard - View Overview Section\",\"Manage Cohort Definitions\",\"View Reports\",\"View Programs\",\"Delete Report Objects\",\"Manage Report Definitions\"],\"baseEntityId\":\"6637559e-ebf9-480a-9731-c47e16e95716\",\"preferredName\":\"Demo test User\",\"voided\":false},\"time\":{\"time\":\"2018-03-02 10:17:51\",\"timeZone\":\"Africa/Harare\"},\"team\":{\"identifier\":\"12345678\",\"person\":{\"gender\":\"F\",\"display\":\"MOH ZEIR Demo\",\"resourceVersion\":\"1.11\",\"dead\":false,\"uuid\":\"12481a02-9a78-4c45-9ead-ddf24d14b19d\",\"birthdateEstimated\":false,\"deathdateEstimated\":false,\"attributes\":[],\"voided\":false,\"links\":[{\"rel\":\"self\",\"uri\":\"http://openmrs.zeir-stage.smartregister.org/openmrs/ws/rest/v1/person/12481a02-9a78-4c45-9ead-ddf24d14b19d\"},{\"rel\":\"full\",\"uri\":\"http://openmrs.zeir-stage.smartregister.org/openmrs/ws/rest/v1/person/12481a02-9a78-4c45-9ead-ddf24d14b19d?v\\u003dfull\"}],\"preferredName\":{\"display\":\"MOH ZEIR Demo\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://openmrs.zeir-stage.smartregister.org/openmrs/ws/rest/v1/person/12481a02-9a78-4c45-9ead-ddf24d14b19d/name/4ab4a8b9-3723-44a8-8733-815ee6d05ef7\"}],\"uuid\":\"4ab4a8b9-3723-44a8-8733-815ee6d05ef7\"}},\"teamMemberId\":1.0,\"patients\":[],\"resourceVersion\":\"1.8\",\"location\":[{\"parentLocation\":{\"display\":\"Fort Jameson\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://openmrs.zeir-stage.smartregister.org/openmrs/ws/rest/v1/location/25089a50-0cf0-47e8-8bfe-fecabed92530\"}],\"uuid\":\"25089a50-0cf0-47e8-8bfe-fecabed92530\"},\"display\":\"Happy Kids Clinic\",\"resourceVersion\":\"1.9\",\"uuid\":\"42abc582-6658-488b-922e-7be500c070f3\",\"tags\":[{\"display\":\"Health Centre Urban\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://openmrs.zeir-stage.smartregister.org/openmrs/ws/rest/v1/locationtag/86c5e41b-08f0-495d-9130-170556c05041\"}],\"uuid\":\"86c5e41b-08f0-495d-9130-170556c05041\"},{\"display\":\"Health Facility\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://openmrs.zeir-stage.smartregister.org/openmrs/ws/rest/v1/locationtag/4d9fce9d-c83a-46aa-b1d9-121da6176758\"}],\"uuid\":\"4d9fce9d-c83a-46aa-b1d9-121da6176758\"}],\"name\":\"Happy Kids Clinic\",\"retired\":false,\"attributes\":[{\"display\":\"dhis_ou_id: k2SgIKwkSh1\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://openmrs.zeir-stage.smartregister.org/openmrs/ws/rest/v1/location/42abc582-6658-488b-922e-7be500c070f3/attribute/01ec1f7c-e061-4f37-9d2c-ce1c7fe99c36\"}],\"uuid\":\"01ec1f7c-e061-4f37-9d2c-ce1c7fe99c36\"}],\"links\":[{\"rel\":\"self\",\"uri\":\"http://openmrs.zeir-stage.smartregister.org/openmrs/ws/rest/v1/location/42abc582-6658-488b-922e-7be500c070f3\"},{\"rel\":\"full\",\"uri\":\"http://openmrs.zeir-stage.smartregister.org/openmrs/ws/rest/v1/location/42abc582-6658-488b-922e-7be500c070f3?v\\u003dfull\"}],\"childLocations\":[{\"display\":\"Happy Kids Clinic: Zone 1\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://openmrs.zeir-stage.smartregister.org/openmrs/ws/rest/v1/location/42b88545-7ebb-4e11-8d1a-3d3a924c8af4\"}],\"uuid\":\"42b88545-7ebb-4e11-8d1a-3d3a924c8af4\"},{\"display\":\"Happy Kids Clinic: Zone 2\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://openmrs.zeir-stage.smartregister.org/openmrs/ws/rest/v1/location/8a40cd7e-b8d4-4c6e-b88f-a77272fec630\"}],\"uuid\":\"8a40cd7e-b8d4-4c6e-b88f-a77272fec630\"},{\"display\":\"Happy Kids Clinic: Zone 3\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://openmrs.zeir-stage.smartregister.org/openmrs/ws/rest/v1/location/5e79ae00-5a69-4814-aace-30e4717f823a\"}],\"uuid\":\"5e79ae00-5a69-4814-aace-30e4717f823a\"},{\"display\":\"Happy Kids Clinic: Zone 4\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://openmrs.zeir-stage.smartregister.org/openmrs/ws/rest/v1/location/e79ff5bc-b6ff-46bc-9bbf-0cedc7d6c4c7\"}],\"uuid\":\"e79ff5bc-b6ff-46bc-9bbf-0cedc7d6c4c7\"}]}],\"team\":{\"teamName\":\"Demo\",\"dateCreated\":\"2017-04-06T09:21:39.000+0200\",\"display\":\"Demo\",\"resourceVersion\":\"1.8\",\"location\":{\"parentLocation\":{\"display\":\"Fort Jameson\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://openmrs.zeir-stage.smartregister.org/openmrs/ws/rest/v1/location/25089a50-0cf0-47e8-8bfe-fecabed92530\"}],\"uuid\":\"25089a50-0cf0-47e8-8bfe-fecabed92530\"},\"display\":\"Happy Kids Clinic\",\"resourceVersion\":\"1.9\",\"uuid\":\"42abc582-6658-488b-922e-7be500c070f3\",\"tags\":[{\"display\":\"Health Centre Urban\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://openmrs.zeir-stage.smartregister.org/openmrs/ws/rest/v1/locationtag/86c5e41b-08f0-495d-9130-170556c05041\"}],\"uuid\":\"86c5e41b-08f0-495d-9130-170556c05041\"},{\"display\":\"Health Facility\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://openmrs.zeir-stage.smartregister.org/openmrs/ws/rest/v1/locationtag/4d9fce9d-c83a-46aa-b1d9-121da6176758\"}],\"uuid\":\"4d9fce9d-c83a-46aa-b1d9-121da6176758\"}],\"name\":\"Happy Kids Clinic\",\"retired\":false,\"attributes\":[{\"display\":\"dhis_ou_id: k2SgIKwkSh1\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://openmrs.zeir-stage.smartregister.org/openmrs/ws/rest/v1/location/42abc582-6658-488b-922e-7be500c070f3/attribute/01ec1f7c-e061-4f37-9d2c-ce1c7fe99c36\"}],\"uuid\":\"01ec1f7c-e061-4f37-9d2c-ce1c7fe99c36\"}],\"links\":[{\"rel\":\"self\",\"uri\":\"http://openmrs.zeir-stage.smartregister.org/openmrs/ws/rest/v1/location/42abc582-6658-488b-922e-7be500c070f3\"},{\"rel\":\"full\",\"uri\":\"http://openmrs.zeir-stage.smartregister.org/openmrs/ws/rest/v1/location/42abc582-6658-488b-922e-7be500c070f3?v\\u003dfull\"}],\"childLocations\":[{\"display\":\"Happy Kids Clinic: Zone 1\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://openmrs.zeir-stage.smartregister.org/openmrs/ws/rest/v1/location/42b88545-7ebb-4e11-8d1a-3d3a924c8af4\"}],\"uuid\":\"42b88545-7ebb-4e11-8d1a-3d3a924c8af4\"},{\"display\":\"Happy Kids Clinic: Zone 2\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://openmrs.zeir-stage.smartregister.org/openmrs/ws/rest/v1/location/8a40cd7e-b8d4-4c6e-b88f-a77272fec630\"}],\"uuid\":\"8a40cd7e-b8d4-4c6e-b88f-a77272fec630\"},{\"display\":\"Happy Kids Clinic: Zone 3\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://openmrs.zeir-stage.smartregister.org/openmrs/ws/rest/v1/location/5e79ae00-5a69-4814-aace-30e4717f823a\"}],\"uuid\":\"5e79ae00-5a69-4814-aace-30e4717f823a\"},{\"display\":\"Happy Kids Clinic: Zone 4\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://openmrs.zeir-stage.smartregister.org/openmrs/ws/rest/v1/location/e79ff5bc-b6ff-46bc-9bbf-0cedc7d6c4c7\"}],\"uuid\":\"e79ff5bc-b6ff-46bc-9bbf-0cedc7d6c4c7\"}]},\"teamIdentifier\":\"Demo\",\"uuid\":\"7bfb4bb3-2689-404c-a5d4-f5cbe1aea9c4\"},\"isTeamLead\":true,\"uuid\":\"6ea953fb-46a2-4415-ae53-299ce909894b\"}}";
        loginResponseData = AssetHandler.jsonStringToJava(userInfoJSON, LoginResponseData.class);
    }

    @Test
    public void shouldGetANMLocation() {
        when(configuration.dristhiBaseURL()).thenReturn("http://opensrp_base_url");
        userService.getLocationInformation();
        verify(httpAgent).fetch("http://opensrp_base_url/teamLocation/teamLocation-tree");
    }

    @Test
    public void shouldSaveUserInformationRemoteLoginIsSuccessful() {
        org.smartregister.domain.jsonmapping.User user = new org.smartregister.domain.jsonmapping.User();
        user.setPreferredName("Test");
        userService.saveUserInfo(user);

        String userInfoString = AssetHandler.javaToJsonString(user);
        verify(saveUserInfoTask).save(userInfoString);
    }

    @Test
    public void shouldSaveANMLocation() {
        org.smartregister.domain.jsonmapping.util.LocationTree locationTree = new org.smartregister.domain.jsonmapping.util.LocationTree();
        org.smartregister.domain.jsonmapping.Location location = new org.smartregister.domain.jsonmapping.Location();
        location.setName("test location");
        location.setLocationId("Test location ID");
        locationTree.addLocation(location);
        userService.saveAnmLocation(locationTree);

        String locationString = AssetHandler.javaToJsonString(locationTree);
        verify(saveANMLocationTask).save(locationString);
    }

    @Test
    public void shouldConsiderALocalLoginValid() {
        // When Username Matches Registered User And Password Matches The One In DB
        when(allSharedPreferences.fetchRegisteredANM()).thenReturn("ANM X");
        when(repository.canUseThisPassword(password)).thenReturn(true);

        Assert.assertTrue(userService.isValidLocalLogin("ANM X", password));

        verify(allSharedPreferences).fetchRegisteredANM();
        verify(repository).canUseThisPassword(password);
    }

    @Test
    public void shouldConsiderALocalLoginInvalidWhenRegisteredUserDoesNotMatch() {
        when(allSharedPreferences.fetchRegisteredANM()).thenReturn("ANM X");

        Assert.assertFalse(userService.isValidLocalLogin("SOME OTHER ANM", "wrong_password".getBytes()));

        verify(allSharedPreferences).fetchRegisteredANM();
        verifyZeroInteractions(repository);
    }

    @Test
    public void shouldConsiderALocalLoginInvalidWhenRegisteredUserMatchesButNotThePassword() {
        when(allSharedPreferences.fetchRegisteredANM()).thenReturn("ANM X");
        when(repository.canUseThisPassword(password)).thenReturn(false);

        Assert.assertFalse(userService.isValidLocalLogin("ANM X", password));

        verify(allSharedPreferences).fetchRegisteredANM();
        verify(repository).canUseThisPassword(password);
    }

    @Test
    public void shouldRegisterANewUser() {
        when(configuration.getDrishtiApplication()).thenReturn(drishtiApplication);
        LoginResponseData userInfo = new LoginResponseData();

        String newUsername = "user X";

        User user = new User();
        user.setUsername(newUsername);
        user.setPassword("password Y");
        userInfo.user = user;

        when(allSharedPreferences.fetchRegisteredANM()).thenReturn("user Z");
        ArgumentCaptor<String> usernameCaptor = ArgumentCaptor.forClass(String.class);

        doReturn(new byte[]{}).when(userService).getDecryptedPassphraseValue(newUsername);

        userService.processLoginResponseDataForUser(newUsername, userInfo);

        verify(allSharedPreferences).updateANMUserName(usernameCaptor.capture());
        String value = usernameCaptor.getValue();
        Assert.assertNotNull(value);
        Assert.assertEquals(newUsername, value);

    }


    public void shouldDeleteDataAndSettingsWhenLogoutHappens() {
        SyncConfiguration syncConfiguration = mock(SyncConfiguration.class);
        Mockito.doReturn(false).when(syncConfiguration).clearDataOnNewTeamLogin();
        ReflectionHelpers.setField(CoreLibrary.getInstance(), "syncConfiguration", syncConfiguration);

        userService.logout();

        verify(repository).deleteRepository();
        verify(repository).deleteRepository();
        verify(repository).deleteRepository();
        verify(allSettings).savePreviousFetchIndex("0");
        verify(allSettings).registerANM("");
    }

    @Test
    public void shouldSwitchLanguageToKannada() {
        when(allSharedPreferences.fetchLanguagePreference()).thenReturn(ENGLISH_LOCALE);

        userService.switchLanguagePreference();

        verify(allSharedPreferences).saveLanguagePreference(KANNADA_LOCALE);
    }

    @Test
    public void shouldSwitchLanguageToEnglish() {
        when(allSharedPreferences.fetchLanguagePreference()).thenReturn(KANNADA_LOCALE);

        userService.switchLanguagePreference();

        verify(allSharedPreferences).saveLanguagePreference(ENGLISH_LOCALE);
    }

    @Test
    public void shouldGetUserDataFromJSON() {

        org.smartregister.domain.jsonmapping.User user = userService.getUserData(loginResponseData);
        Assert.assertNotNull(user);

        Assert.assertEquals("demotest", user.getUsername());
        Assert.assertEquals("Demo test User", user.getPreferredName());
    }

    @Test
    public void shouldGetUserLocationFromJSON() {

        org.smartregister.domain.jsonmapping.util.LocationTree locationTree = userService.getUserLocation(loginResponseData);

        LinkedHashMap<String, org.smartregister.domain.jsonmapping.util.TreeNode<String, org.smartregister.domain.jsonmapping.Location>> mapLocation = locationTree.getLocationsHierarchy();

        Assert.assertEquals("Pakistan", mapLocation.values().iterator().next().getLabel());
    }

    @Test
    public void testGetServerTimeZoneWithTime() {
        loginResponseData = mock(LoginResponseData.class);
        loginResponseData.time = new Time(new Date(), TimeZone.getTimeZone("Africa/Nairobi"));
        TimeZone timezone = UserService.getServerTimeZone(loginResponseData);
        Assert.assertNotNull(timezone);
        long millisecondsPerHour = 3600 * 1000;
        Assert.assertEquals(3 * millisecondsPerHour, timezone.getRawOffset());
        Assert.assertEquals("Africa/Nairobi", timezone.getID());
    }

    @Test
    public void testValidateDeviceTimeForNullServerTimeZoneReturnsError() {
        loginResponseData = mock(LoginResponseData.class);
        Assert.assertEquals(TimeStatus.ERROR, userService.validateDeviceTime(loginResponseData, 3600));
    }

    @Test
    public void testValidateDeviceTimeForDifferentTimeZoneServerTimeZoneReturnsMismatch() {
        loginResponseData = mock(LoginResponseData.class);
        loginResponseData.time = new Time(new Date(), TimeZone.getTimeZone("Africa/Nairobi"));
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        Assert.assertEquals(TimeStatus.TIMEZONE_MISMATCH, userService.validateDeviceTime(loginResponseData, 3600 * 1000));
    }

    @Test
    public void testValidateDeviceTimeForDifferentTimeReturnsMismatch() {
        loginResponseData = mock(LoginResponseData.class);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -30);
        loginResponseData.time = new Time(calendar.getTime(), TimeZone.getTimeZone("Africa/Nairobi"));
        TimeZone.setDefault(TimeZone.getTimeZone("Africa/Nairobi"));
        Assert.assertEquals(TimeStatus.TIME_MISMATCH, userService.validateDeviceTime(loginResponseData, 15 * 1000));
    }

    @Test
    public void testValidateDeviceTimeSameTimeTimeAndTimeZone() {
        loginResponseData = mock(LoginResponseData.class);
        Calendar calendar = Calendar.getInstance();
        loginResponseData.time = new Time(calendar.getTime(), TimeZone.getTimeZone("Africa/Nairobi"));
        TimeZone.setDefault(TimeZone.getTimeZone("Africa/Nairobi"));
        Assert.assertEquals(TimeStatus.OK, userService.validateDeviceTime(loginResponseData, 15 * 1000));
    }


    @Test
    public void testValidateStoredServerTimeZoneForNullServerTimeZoneReturnsError() {
        when(allSharedPreferences.fetchServerTimeZone()).thenReturn(null);
        Assert.assertEquals(TimeStatus.ERROR, userService.validateStoredServerTimeZone());
    }

    @Test
    public void testValidateStoredServerTimeZoneForDifferentTimeZoneServerTimeZoneReturnsMismatch() {
        when(allSharedPreferences.fetchServerTimeZone()).thenReturn("Africa/Nairobi");
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        Assert.assertEquals(TimeStatus.TIMEZONE_MISMATCH, userService.validateStoredServerTimeZone());
    }


    @Test
    public void testValidateStoredServerTimeZoneSameTimeTimeAndTimeZone() {
        when(allSharedPreferences.fetchServerTimeZone()).thenReturn("Africa/Nairobi");
        TimeZone.setDefault(TimeZone.getTimeZone("Africa/Nairobi"));
        Assert.assertEquals(TimeStatus.OK, userService.validateStoredServerTimeZone());
    }

}
