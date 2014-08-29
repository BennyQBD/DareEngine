package core;

public class CoreEngine
{
	private Display  m_display;
	private Game     m_game;

	public CoreEngine(Display display, Game game)
	{
		m_display = display;
		m_game    = game;
	}

	public void Start()
	{
		long previousTime = System.nanoTime();
		while(true)
		{
			long currentTime = System.nanoTime();
			float delta = (float)((currentTime - previousTime)/1000000000.0);
			previousTime = currentTime;

			m_game.Update(m_display.GetInput(), delta);
			m_game.Render(m_display.GetContext());
			
			m_display.SwapBuffers();
			try
			{
				Thread.sleep(1);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
}
