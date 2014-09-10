package engine.components;

import engine.core.*;
import engine.rendering.*;

public class SpriteComponent extends EntityComponent
{
	public static final String NAME = "SpriteComponent";

	private Bitmap[] m_frames;
	private float[]  m_frameTimes;
	private int[]    m_nextFrames;
	private int      m_currentFrame;
	private int      m_transparencyType;
	private float    m_renderLayer;
	private float    m_currentFrameTime;

	public SpriteComponent(Bitmap sprite, int transparencyType, float layer)
	{
		this(new Bitmap[] { sprite }, 0.0f, transparencyType, layer);
	}

	public SpriteComponent(Bitmap[] frames, float frameTime, 
			int transparencyType, float layer)
	{
		super(NAME);
		float frameTimes[] = new float[frames.length];
		int nextFrames[] = new int[frames.length];

		for(int i = 0; i < frames.length; i++)
		{
			frameTimes[i] = frameTime;
			nextFrames[i] = i + 1;
		}
		nextFrames[frames.length - 1] = 0;
		Init(frames, frameTimes, nextFrames, transparencyType, layer);
	}
	
	private final void Init(Bitmap[] frames, 
			float[] frameTimes, int[] nextFrames,
			int transparencyType, float layer)
	{
		m_frames = frames;
		m_frameTimes = frameTimes;
		m_nextFrames = nextFrames;
		m_transparencyType = transparencyType;
		m_renderLayer = layer;
		m_currentFrame = 0;
		m_currentFrameTime = 0.0f;
	}

	public SpriteComponent(Bitmap[] frames, 
			float[] frameTimes, int[] nextFrames,
			int transparencyType, float layer)
	{
		super(NAME);
		Init(frames, frameTimes, nextFrames, transparencyType, layer);
	}

	@Override
	public void OnAdd()
	{
		GetEntity().setRenderLayer(m_renderLayer);
	}

	@Override
	public void Update(Input input, float delta)
	{
		m_currentFrameTime += delta;
		while(m_frameTimes[m_currentFrame] != 0 
			&& m_currentFrameTime > m_frameTimes[m_currentFrame])
		{
			m_currentFrameTime -= m_frameTimes[m_currentFrame];
			int nextFrame = m_nextFrames[m_currentFrame];
			m_currentFrame = nextFrame;
		}
	}

	@Override
	public void Render(RenderContext target)
	{
		target.DrawImage(m_frames[m_currentFrame],
				GetEntity().getAABB().GetMinX(), 
				GetEntity().getAABB().GetMinY(),
		        GetEntity().getAABB().GetMaxX(), 
				GetEntity().getAABB().GetMaxY(),
				m_transparencyType
			);
	}
}
