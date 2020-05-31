package org;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import jbse.apps.run.Run;
import jbse.apps.run.RunParameters;
import jbse.apps.run.RunParameters.DecisionProcedureType;
import jbse.apps.run.RunParameters.StateFormatMode;
import jbse.apps.run.RunParameters.StepShowMode;

import java.util.ArrayList;
import java.util.List;

public class Main {

    @Parameter(names = "-jbse_home")
    private String JBSE_HOME;

    @Parameter(names = "-z3_path")
    private String Z3_PATH;

    @Parameter(names = "-test_home")
    private String TEST_HOME;

    @Parameter(names = {"-method_class", "-mc"})
    private String METHOD_CLASS;

    @Parameter(names = {"-method_descriptor", "-md"})
    private List<String> METHOD_DESCRIPTOR = new ArrayList<>();

    @Parameter(names = {"-method_name", "-mn"})
    private List<String> METHOD_NAME = new ArrayList<>();

    @Parameter(names = {"-test_name", "-tn"})
    private List<String> TEST_NAME = new ArrayList<>();

    @Parameter(names = "-jre")
    private String JRE_SOURCEPATH;

    private String[] CLASSPATH;
    private String[] SOURCEPATH;

    public static void main(String[] args) {
        final Main main = new Main();
        JCommander.newBuilder().addObject(main).build().parse(args);
        main.start();
    }

    private void start(){
        final String TARGET_CLASSPATH = TEST_HOME + "bin/";
        final String TARGET_SOURCEPATH = TEST_HOME + "src/";
        final String JBSE_SOURCEPATH = JBSE_HOME + "src/main/java/";
        final String JBSE_CLASSPATH = JBSE_HOME + "build/classes/java/main";
        JRE_SOURCEPATH += "src.zip";
        CLASSPATH       = new String[]{TARGET_CLASSPATH};
        SOURCEPATH      = new String[]{JBSE_SOURCEPATH, TARGET_SOURCEPATH, JRE_SOURCEPATH};

        for (int i = 0; i < METHOD_DESCRIPTOR.size(); i++) {
            final RunParameters p = new RunParameters();
            set(p, i, JBSE_CLASSPATH);
            final Run r = new Run(p);
            r.run();
        }
    }

    private void set(final RunParameters p, final int i, final String classpath) {
        p.setJBSELibPath(classpath);
        p.addUserClasspath(CLASSPATH);
        p.addSourcePath(SOURCEPATH);
        p.setMethodSignature(METHOD_CLASS, METHOD_DESCRIPTOR.get(i), METHOD_NAME.get(i));
        String OUT_FILE = TEST_HOME + TEST_NAME.get(i);
        p.setOutputFileName(OUT_FILE);
        p.setDecisionProcedureType(DecisionProcedureType.Z3);
        p.setExternalDecisionProcedurePath(Z3_PATH);
        p.setStateFormatMode(StateFormatMode.JUNIT_TEST);
        p.setStepShowMode(StepShowMode.LEAVES);
    }
}
