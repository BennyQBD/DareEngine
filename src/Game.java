import java.awt.event.KeyEvent;
import java.util.*;

public class Game
{
	public Game()
	{
	}

	public void Update(Input input, float delta)
	{
	}

	public void Render(RenderContext target)
	{
		target.FillRect(0.0f, 0.0f, 0.1f, 0.1f,
				(byte)0x00, 
				(byte)0x79, (byte)0xbf, (byte)0x10);
	}
}
