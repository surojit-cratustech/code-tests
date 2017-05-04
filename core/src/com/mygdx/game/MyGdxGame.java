package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.SphereShapeBuilder;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;

public class MyGdxGame extends ApplicationAdapter {
    public static final int TEXTURE_WIDTH = 128;
    public static final int TEXTURE_HEIGHT = 128;
    SpriteBatch batch;
	Texture img;
	private ModelBatch mBatch;
	private PerspectiveCamera mCamera;
	private Model ballModel;
	private ModelInstance ballInstance;
    private Texture dynaText;
    private BitmapFont aFont;
    private FrameBuffer lFb;
    private Model cylModel;
    private ModelInstance cylInst;

    @Override
	public void create () {
		mCamera = new PerspectiveCamera(67,Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		mCamera.update();
		batch = new SpriteBatch();

		mBatch = new ModelBatch();
				
		img = new Texture("badlogic.jpg");

//        dynaText = new Texture(128,32, Pixmap.Format.RGBA8888);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/Lato-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16;
        parameter.color = Color.GREEN;
        parameter.magFilter = Texture.TextureFilter.Linear; // used for resizing quality
        parameter.minFilter = Texture.TextureFilter.Linear;
        generator.scaleForPixelHeight(8);

        aFont = generator.generateFont(parameter);
        aFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        lFb = new FrameBuffer(Pixmap.Format.RGBA4444, TEXTURE_WIDTH, TEXTURE_HEIGHT,false);

        lFb.begin();

//        Gdx.gl.glViewport(0,0,-TEXTURE_WIDTH/2,-TEXTURE_HEIGHT/2);
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.4f, 1);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        aFont.setColor(Color.BLUE);
        Matrix4 lm = new Matrix4();
        lm.setToOrtho2D(0,0,128,-128);
        batch.setProjectionMatrix(lm);
        batch.begin();
        aFont.draw(batch,"Goal!",64,-64);
        batch.end();
        lFb.end();
//        aFont.draw(batch, options.get(i), aBound.getCenterX(), aBound.getCenterY() + textHeight / 2);


		ModelBuilder mb = new ModelBuilder();
        Texture lTexture = lFb.getColorBufferTexture();
        lTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        Material lMaterial = new Material(TextureAttribute.createDiffuse(lTexture));

		ballModel = mb.createSphere(1.0f,1.0f,1.0f,16,16,lMaterial, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

        cylModel = mb.createCapsule(1.0f,5.0f,16,lMaterial, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

        cylInst = new ModelInstance(cylModel);
        ballInstance = new ModelInstance(ballModel);

        ballInstance.transform.translate(-0.5f,0,-1.5f);
        cylInst.transform.rotate(0,0,1.0f,-90f);
        cylInst.transform.translate(2f,0,-4.5f);
	}


	@Override
	public void render () {

//		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

//        batch.setProjectionMatrix(mCamera.combined);
        batch.begin();
        batch.draw(lFb.getColorBufferTexture(),0,0);
		batch.draw(img,Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
//        aFont.draw(batch,"WORLD!", -1.0f,10.0f);
		batch.end();

		mBatch.begin(mCamera);
		mBatch.render(ballInstance);
        mBatch.render(cylInst);
		mBatch.end();
		ballInstance.transform.rotate(0.0f,1.0f,0.0f,0.5f);
        cylInst.transform.rotate(0.0f,1.0f,0.0f,0.5f);
//		ballInstance.transform.translate(0,0,-0.05f);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
