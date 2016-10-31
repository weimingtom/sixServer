/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ngame.job;

import org.ngame.service.JobService;

/**
 *
 * @author Administrator
 */
public class TestJob extends GameJob
{

	public TestJob()
	{
	}

	public TestJob(String name, String group)
	{
		super(name, group);
	}

	@Override
	protected void process()
	{
		System.out.println("...");
		System.out.println(name + ":" + group);
	}

	@Override
	public void immediatelyProcess(String name, String group, long remainTime)
	{
	}
	private static JobService s = new JobService();

	public static void main(String... arg)
	{
		s.addJob(new TestJob("test", "test"), 5000);
		System.out.println(s.getRemainTime("test", "test"));
	}
}
