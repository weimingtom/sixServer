using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using NTFrame;
using CommonDelegate;

public class HttpUtil : Singleton<HttpUtil> {
	public const string ERROR = "error";

	List<HttpTime> _HttpQuene = new List<HttpTime>();
	List<HttpTime> _RemoveQuene = new List<HttpTime>();

	public class HttpTime
	{
		public WWW www;
		public StringDelegate del;
		public float timeout;
		public HttpTime(WWW www, StringDelegate del, float timeout)
		{
			this.www = www;
			this.del = del;
			this.timeout = timeout;
		}
	}


	/// <summary>
	/// 使用www发送post请求
	/// </summary>
	/// <param name="url"></param>
	/// <param name="form"></param>
	/// <param name="del"></param>
	/// <param name="timeout"></param>
	public void POST(string url, WWWForm form, StringDelegate del, int timeout)
	{
		// 相同的url认为是重复请求，过滤掉
		for (int i = 0; i < _HttpQuene.Count; i++) {
			if(_HttpQuene[i].www != null && _HttpQuene[i].www.url == url)
			{
				return;
			}
		}
		if (form == null)
		{
			return;
		}
		WWW www = new WWW(url, form);
		
		if (IsInQuene(www))
		{
			return;
		}
		HttpTime httpTime = new HttpTime(www, del, timeout);
		_HttpQuene.Add(httpTime);
	}

	/// <summary>
	/// 使用www发送get请求
	/// </summary>
	/// <param name="url"></param>
	/// <param name="del"></param>
	/// <param name="timeout"></param>
	public void GET(string url, StringDelegate del, int timeout)
	{
		// 相同的url认为是重复请求，过滤掉
		for (int i = 0; i < _HttpQuene.Count; i++) {
			if(_HttpQuene[i].www != null && _HttpQuene[i].www.url == url)
			{
				return;
			}
		}
		WWW www = new WWW(url);

		if (IsInQuene(www))
		{
			return;
		}
		HttpTime httpTime = new HttpTime(www, del, timeout);
		_HttpQuene.Add(httpTime);
	}

	public void Update()
	{
		// 每10帧执行一次就行了。
		if (Time.frameCount % 10 != 0)
		{
			return;
		}
		for (int i = 0; i < _HttpQuene.Count; i++) {
			if (_HttpQuene[i] != null && _HttpQuene[i].www != null)
			{
				if (_HttpQuene[i].del != null)
				{
					if (_HttpQuene[i].timeout <= 0)
					{
						_HttpQuene[i].del(ERROR + " timeout");
						_RemoveQuene.Add(_HttpQuene[i]);
						continue;
					}
					_HttpQuene[i].timeout -= Time.deltaTime * 10 * 1000;
					if (string.IsNullOrEmpty(_HttpQuene[i].www.error) == false)
					{
#if UNITY_EDITOR
						Debuger.LogError(_HttpQuene[i].www.error + "====" + _HttpQuene[i].www.url);
#endif
						_HttpQuene[i].del(ERROR + "-" + _HttpQuene[i].www.error);
						_RemoveQuene.Add(_HttpQuene[i]);
					}
					else
					{
						if (_HttpQuene[i].www.isDone)
						{
							_HttpQuene[i].del(_HttpQuene[i].www.text);
							_RemoveQuene.Add(_HttpQuene[i]);
						}
					}
				}
				else
				{
					_RemoveQuene.Add(_HttpQuene[i]);
				}
			}
		}

		for (int i = 0; i < _RemoveQuene.Count; i++)
		{
			_HttpQuene.Remove(_RemoveQuene[i]);
		}
		_RemoveQuene.Clear();
	}

	public bool IsInQuene(WWW www)
	{
		if(www == null)
		{
			return false;
		}
		for (int i = 0; i < _HttpQuene.Count; i++) {
			if(_HttpQuene[i].www == null)
			{
				continue;
			}
			if(_HttpQuene[i].www == www)
			{
				return true;
			}
		}

		return false;
	}

	public void Clear()
	{
		for (int i = 0; i < _HttpQuene.Count; i++)
		{
			_HttpQuene[i].www = null;
		}
		_HttpQuene.Clear();
	}
}
