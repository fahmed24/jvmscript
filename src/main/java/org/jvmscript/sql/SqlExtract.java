package org.jvmscript.sql;

import org.jvmscript.email.EmailMessage;
import static org.jvmscript.cli.CliUtility.*;
import static org.jvmscript.sql.SqlUtility.*;
import static org.jvmscript.email.EmailUtility.*;
import static org.jvmscript.file.FileUtility.*;

public class SqlExtract {
    public static void main(String[] args) throws Exception {
        String fileExtension = ".csv";
        boolean excelOutput = false;

        cliUtilityInitialize();
        cliAddOption("propertyFile", true, true);
        cliAddOption("sqlFile", true, true);
        cliAddOption("outputDir", true, false);
        cliAddOption("delimiteParam", true, false);
        cliAddOption("excel", false, false);
        cliAddOption("emailList", true, false);
        cliParse(args);

        SqlUtility.delimiter = cliGetOptionValue("delimiterParam", ",").charAt(0);
        if (cliHasOption("excel")) excelOutput = true;

        if (SqlUtility.delimiter == ',') fileExtension = ".txt";
        if (excelOutput) fileExtension = ".xlsx";

        String sqlFilename = cliGetOptionValue("sqlFile");
        String outputDir = cliGetOptionValue("outputDir", "");
        String outputFilename = outputDir + getFileBaseName(sqlFilename) + fileExtension;

        if (cliHasOption("propertyFile"))
            openSqlConnection(cliGetOptionValue("propertyFile"));
        else
            openSqlConnection();

        if (excelOutput)
            exportSqlFileQueryToExcel(sqlFilename, outputFilename);
        else
            exportSqlFileQueryToFile(sqlFilename, outputFilename);

        closeSqlConnection();

        if (cliHasOption("emailList")) {
            EmailMessage emailMessage = createEmailMessage();
            emailMessage.addToRecipient(cliGetOptionValue("emailList"));
            emailMessage.setSubject("COR Report/Extract Runner " + getFileName(outputFilename));
            emailMessage.setBody("COR Report/Extract Runner " + getFileName(outputFilename));
            emailMessage.addAttachment(outputFilename);

            openSmtpConnection();
            sendEmailMessage(emailMessage);
            closeSmtpConnection();
        }
    }
}
