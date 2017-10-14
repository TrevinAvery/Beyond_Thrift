package com.trevinavery.beyondthrift;

public class TestDriver {

    public static void main(String[] args) {

        org.junit.runner.JUnitCore.main(
                "com.trevinavery.familymap.dao.AuthTokenDaoTest",
                "com.trevinavery.familymap.dao.EventDaoTest",
                "com.trevinavery.familymap.dao.PersonDaoTest",
                "com.trevinavery.familymap.dao.UserDaoTest",
                "com.trevinavery.familymap.service.ClearServiceTest",
                "com.trevinavery.familymap.service.EventServiceTest",
                "com.trevinavery.familymap.service.FillServiceTest",
                "com.trevinavery.familymap.service.LoadServiceTest",
                "com.trevinavery.familymap.service.LoginServiceTest",
                "com.trevinavery.familymap.service.PersonServiceTest",
                "com.trevinavery.familymap.service.RegisterServiceTest"
        );
    }
}