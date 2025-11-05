package com.patient;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Patient Management System Test Suite")
@SelectPackages({
    "com.patient.service",
    "com.patient.controller",
    "com.patient.repository"
})
public class TestSuite {
    // This class runs all tests in the specified packages
}