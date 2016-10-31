     /// <summary>
    /// ÇëÇóµÇÂ¼µÄtoken
    /// </summary>
    /// <param name="account"></param>
    /// <param name="pwd"></param>
    /// <param name="server_id"></param>
 public void Login(string account, string pwd)
    {
        ServerInfo serverInfo = ServerListManager.GetInstance().GetCurrentServerInfo();
        if (serverInfo != null)
        {
            string server_id = ServerListManager.GetInstance().GetCurrentServerInfo().server_id;
            string url = string.Format(_LoginUrl, AssetCoreManager.GetInstance().Version.LoginServer, WWW.EscapeURL(account), WWW.EscapeURL(pwd), server_id);
            Debuger.Log(url);
            HttpUtil.Instance.Clear();
            HttpUtil.GetInstance().GET(url, LoginManager.GetInstance().OnLogin, 10000);
        }
        else
        {
            ServerListManager.GetInstance().GetServerList(null);
        }
    }
	
	
	
	  /// <summary>
    /// ÕËºÅ×¢²á
    /// </summary>
    /// <param name="account"></param>
    /// <param name="pwd"></param>
    public void AccountRegister(string account, string pwd)
    {
        string url = string.Format(_AccountRegistUrl, AssetCoreManager.GetInstance().Version.LoginServer, WWW.EscapeURL(account), WWW.EscapeURL(pwd));
        HttpUtil.Instance.Clear();
        HttpUtil.GetInstance().GET(url, LoginManager.GetInstance().OnRegister, 5000);
    }
