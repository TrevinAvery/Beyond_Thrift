package com.trevinavery.beyondthrift.service;

import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import com.trevinavery.beyondthrift.dao.Database;
import fms.model.User;
import fms.request.FillRequest;
import fms.request.LoadRequest;
import fms.result.LoadResult;

/**
 * Created by trevinpa on 5/31/17.
 */
public class LoadServiceTest {

    private Database database;

    @Before
    public void setUp() throws Exception {
        database = new Database();
        database.setLockedOpen(true);
        database.open();
        database.clear();
    }

    @After
    public void tearDown() throws Exception {
        database.setLockedOpen(false);
        database.close(false);
    }

    @Test
    public void load() throws Exception {

        Gson gson = new Gson();

        LoadRequest request = gson.fromJson(
                new InputStreamReader(
                        new FileInputStream(
                                new File("json/example.json"))), LoadRequest.class);

        LoadService service = new LoadService(database);

        LoadResult result = service.perform(request);

        LoadResult expectedResult = new LoadResult(
                "Successfully added 1 users, 3 persons, and 2 events to the database."
        );

        assertEquals(expectedResult, result);
    }

    @Test
    public void loadEmptyData() throws Exception {

        LoadRequest request = new LoadRequest(
                null,
                null,
                null
        );

        LoadService service = new LoadService(database);

        LoadResult result = service.perform(request);

        LoadResult expectedResult = new LoadResult(
                "Successfully added 0 users, 0 persons, and 0 events to the database."
        );

        assertEquals(expectedResult, result);
    }

    @Test
    public void loadInvalidData() throws Exception {

        User user = new User(
                "username",
                "password",
                "email@site.com",
                "firstname",
                "lastname",
                "m",
                "personid"
        );

        LoadRequest request = new LoadRequest(
                new User[] {
                        user,
                        user
                },
                null,
                null
        );

        LoadService service = new LoadService(database);

        LoadResult result = service.perform(request);

        LoadResult expectedResult = new LoadResult(
                "Attempted to enter a user that already exists"
        );

        assertEquals(expectedResult, result);
    }

    @Test
    public void loadIncompleteData() throws Exception {

        User user = new User(
                null,
                "password",
                "email@site.com",
                "firstname",
                "lastname",
                "m",
                "personid"
        );

        LoadRequest request = new LoadRequest(
                new User[] {
                        user,
                        user
                },
                null,
                null
        );

        LoadService service = new LoadService(database);

        LoadResult result = service.perform(request);

        LoadResult expectedResult = new LoadResult(
                "Invalid values were included in the load data"
        );

        assertEquals(expectedResult, result);
    }

}