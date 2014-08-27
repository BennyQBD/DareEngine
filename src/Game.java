import java.awt.event.KeyEvent;
import java.util.*;

public class Game
{
	public Game()
	{
		Entity test1 = new Entity(-1, -1, 1, 1);

		QuadTree tree = new QuadTree(test1, new AABB(-100, -100, 100, 100));
		tree.Add(new Entity(-3, -3, 0, 0));
		tree.Add(new Entity(-3, 0, 0, 3));
		tree.Add(new Entity(-1, -1, 1, 1));

		Set<Entity> testSet = tree.QueryRange(new AABB(-2, -2, 0, 0));
		Iterator it = testSet.iterator();
		while(it.hasNext()) 
		{
			System.out.println(it.next());
		}
	}

	public void Update(Input input, float delta)
	{
	}

	public void Render(RenderContext target)
	{
		target.Clear((byte)0x00);
		target.FillRect(0.0f, 0.0f, 0.1f, 0.1f,
				(byte)0x00, 
				(byte)0x79, (byte)0xbf, (byte)0x10);
	}
}
