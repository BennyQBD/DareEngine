package engine.core;

import java.util.Random;
import engine.rendering.*;

/* 
 * Keeps track of the various game engine components, and controls when they
 * take action.
 */
public class CoreEngine implements Runnable
{
	private final Thread  m_thread;    // The primary thread of execution
	private final Display m_display;   // Where any graphics are displayed
	private final Scene   m_scene;     // The scene that the engine is running;
	private boolean       m_isRunning; // Whether the engine is currently running or not.

	public CoreEngine(Display display, Scene scene)
	{
		m_display = display;
		m_scene = scene;
		m_thread = new Thread(this);
		m_isRunning = false;

		//Display something immediately; helps reduce first frame issues.
		m_display.SwapBuffers();
		m_display.SwapBuffers();
	}

	/*
	 * Begins running all the various components.
	 */
	public void start()
	{
		if(m_isRunning)
		{
			return;
		}
		m_isRunning = true;

		m_thread.start();
	}

	/*
	 * Stops running all the various components.
	 */
	public void stop()
	{
		if(!m_isRunning)
		{
			return;
		}
		m_isRunning = false;

		try
		{
			m_thread.join();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			System.exit(0);
		}
	}

	/*
	 * Runs all the components.
	 * Should not be called directly; is only public
	 * because the Runnable interface requires it to be.
	 */
	public void run()
	{
		int frames = 0;
		double unprocessedTime = 0.0;
		double secondsPerFrame = 1.0/60.0;
		double frameCounterTime = 0;

		long previousTime = System.nanoTime();
		String fpsString = "0 ms per frame (0 fps)";
		while(m_isRunning)
		{
			boolean render = false;
			long currentTime = System.nanoTime();
			long passedTime = currentTime - previousTime;
			previousTime = currentTime;
			//float delta = (float)(passedTime / 1000000000.0);
			unprocessedTime  += passedTime / 1000000000.0;
			frameCounterTime += passedTime / 1000000000.0;

			if(frameCounterTime >= 1.0)
			{
				fpsString = (1000.0/frames) + " ms per frame (" + frames + " fps)";
				System.out.println(fpsString);
				
				frames = 0;
				frameCounterTime = 0.0;
			}
			while(unprocessedTime > secondsPerFrame)
			{
				render = true;
				//m_display.Update();
				m_scene.Update(m_display.GetInput(), (float)secondsPerFrame);
				unprocessedTime -= secondsPerFrame;
			}

			if(render || true)
			{
				frames++;

				RenderContext context = m_display.GetContext();
				m_scene.Render(context);
				float fontSize = 16.0f/256.0f;
				context.DrawString(fpsString, -1.0f, 1.0f - fontSize, fontSize,
					(byte)0xFF, (byte)0xFF, (byte)0xFF);
				m_display.SwapBuffers();
			}
			else
			{
				try
				{
					Thread.sleep(1);
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
					System.exit(1);
				}
			}
		}
		//m_display.Dispose();
	}
}
