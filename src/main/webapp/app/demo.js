var ews = require("ews-javascript-api");
var ewsAuth = require("ews-javascript-api-auth");
ews.EwsLogging.DebugLogEnabled = false;
ews.ConfigurationApi.ConfigureXHR(new ewsAuth.cookieAuthXhrApi(credentials.userName, credentials.password));
exch.Url = new ews.Uri("https://outlook.office365.com/Ews/Exchange.asmx");
exch.ImpersonatedUserId = new
ews.ImpersonatedUserId(ews.ConnectingIdType.SmtpAddress, "user@domain.com");
exch.HttpHeaders = { "X-AnchorMailbox": "user@domain.com" };
var rule = new ews.Rule();
rule.DisplayName = "MoveInterestingToJunk";
rule.Priority = 1;
rule.IsEnabled = true;
rule.Conditions.ContainsSubjectStrings.Add("Interesting");
rule.Actions.MoveToFolder = new ews.FolderId(ews.WellKnownFolderName.JunkEmail);
var ruleop = new ews.CreateRuleOperation(rule);
exch.UpdateInboxRules([ruleop], true)
    .then(function (response) {
        console.log("success - update-inboxrules");
        ews.EwsLogging.Log(response, true, true);
    }, function (err) {
        debugger;
        console.log("error in update-inboxrules");
        ews.EwsLogging.Log(err, true, true);
    });
