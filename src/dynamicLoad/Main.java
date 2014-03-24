package dynamicLoad;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix4f;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Quad;
import com.jme3.system.AppSettings;
import com.jme3.terrain.Terrain;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.texture.Texture2D;
import com.jme3.water.SimpleWaterProcessor;
import com.jme3.water.WaterFilter;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedEvent;
import staticClasses.Variables;

/**
 * test
 * @author Hamza ABED 2014
 * hamza.abed.professionel@gmail.com
 */
public class Main extends SimpleApplication implements AnalogListener,AnimEventListener{

    
    
  private AnimChannel channel;
  private AnimControl control;
  private CharacterControl player;
  private  Node sinbadPlayer;
  private Vector3f lightDirection=new Vector3f(-4,-1,5);
  private Spatial sceneModel;
  private BulletAppState bulletAppState;
  private RigidBodyControl landscape;
  private boolean left = false, right = false, up = false, down = false;
  private Vector3f camDir  = new Vector3f();
  private Vector3f camLeft = new Vector3f();
  private Vector3f walkDirection = new Vector3f();
  private static Main app;
  
  
  
  public static void main(String[] args) {
        app = new Main();
        AppSettings settings = new AppSettings(true);
    settings.setResolution(1024, 763);
    app.setShowSettings(false); // splashscreen
    app.setSettings(settings);
    
        app.start();
    }
    private Quad quad;
    private WaterFilter water;

    public Main() {
        this.actionListener = new ActionListener() { 
public void onAction(String name, boolean keyPressed, float tpf) {
if (name.equals("addObject")) {
//Node fireCamp=(Node) assetManager.loadModel("Models/Oto/Oto.mesh.xml");
Spatial fireCamp = assetManager.loadModel("Models/campfire/campfire.j3o");
rootNode.attachChild(fireCamp);
fireCamp.setLocalTranslation(new Vector3f(player.getPhysicsLocation().x+30,player.getPhysicsLocation().y,player.getPhysicsLocation().z));
System.out.println("child attached !!");

}
if(name.equals("LClick"))
{
afficherFlecheDestination();
}

}
};  }

 private Arrow arrow;

 public void afficherFlecheDestination()
    {
        
///This is about arrow
arrow = new Arrow(Vector3f.UNIT_Y);
arrow.setLineWidth(4); // make arrow thicker

Vector3f origin    = sinbadPlayer.getLocalTranslation();
Vector3f direction = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.3f);


Vector3f location = direction.subtractLocal(origin).normalizeLocal();
//putShape(arrow, ColorRGBA.Green).setLocalTranslation(
//new Vector3f(location.x, location.y,location.z)
//); 
//System.out.println("location "+location + "sinbad "+sinbadPlayer.getLocalTranslation());


/////
 // 1. Reset results list.
CollisionResults results = new CollisionResults();
Vector2f click2d = inputManager.getCursorPosition();
Vector3f click3d = cam.getWorldCoordinates(
    new Vector2f(click2d.x, click2d.y), 0f).clone();
Vector3f dir = cam.getWorldCoordinates(
    new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
Ray ray = new Ray(click3d, dir);
sceneModel.collideWith(ray, results);



//        CollisionResults results = new CollisionResults();
        // 2. Aim the ray from cam loc to cam direction.
  //      Ray ray = new Ray(cam.getLocation(), cam.getDirection());
        // 3. Collect intersections between Ray and Shootables in results list.
        sceneModel.collideWith(ray, results);
        // 4. Print the results
        System.out.println("----- Collisions? " + results.size() + "-----");
        Vector3f pt=null;
        for (int i = 0; i < results.size(); i++) {
          // For each hit, we know distance, impact point, name of geometry.
          float dist = results.getCollision(i).getDistance();
          pt = results.getCollision(i).getContactPoint();
          String hit = results.getCollision(i).getGeometry().getName();
          System.out.println("* Collision #" + i);
          System.out.println("  You shot " + hit + " at " + pt + ", " + dist + " wu away.");
        }
        System.out.println("sinbad "+sinbadPlayer.getLocalTranslation());
        // 5. Use the results (we mark the hit object)
        
        
       // if(arrow!=null) rootNode.detachChild(arrow);
        removeArrow();
        putShape(arrow, ColorRGBA.Green).setLocalTranslation(new Vector3f(pt.x, pt.y,pt.z));
        
        //rootNode.detachChild(sceneModel);

///

    }
    @Override
    public void simpleInitApp() {
       
    app.setDisplayFps(false);
    app.setDisplayStatView(false);
    bulletAppState = new BulletAppState(); //Ceci c'est pour spécifier 
    stateManager.attach(bulletAppState); //qu'on va travailler avec des physics
    
   initNiftyGUI();  
    
   
        initLight();
        initScene();
        initPlayer();
        
        
        setUpKeys();
        viewPort.setBackgroundColor(new ColorRGBA(0.7f,0.8f,1f,1f));
        flyCam.setMoveSpeed(150);
       // cam.setLocation(lightDirection);
       // channel.reset(true); // this stop the animation
      
        //***** DISABLING THE FLYING CAMERA  ******///
       flyCam.setEnabled(false);
       ChaseCamera chaseCam = new ChaseCamera(cam, sinbadPlayer, inputManager);
      
       // bulletAppState.getPhysicsSpace().enableDebug(assetManager);
        afficherTexte("Version d'essai");
       initSimpleWater();initPPcWater();       
      
 
    }
    
    
    private void initPlayer()
    {
          sinbadPlayer =(Node) assetManager.loadModel("Models/high-sinbad/Sinbad.mesh.xml");
        sinbadPlayer.setLocalScale(0.5f);
        rootNode.attachChild(sinbadPlayer);
        sinbadPlayer.move(-10, 5, -10); 
        
        control = sinbadPlayer.getControl(AnimControl.class);
        control.addListener((AnimEventListener) this);
        channel = control.createChannel();
        channel.setAnim("idle");
        
         CapsuleCollisionShape capsuleShape = 
         new CapsuleCollisionShape(2f, 1f, 1);
        
        
        player = new CharacterControl(capsuleShape, 0.1f);
        player.setJumpSpeed(30);
        player.setFallSpeed(600);
        player.setGravity(50);
        
        player.setPhysicsLocation(new Vector3f(-10, 5, -10));
        // sinbadPlayer.addControl(player);
       
        
        bulletAppState.getPhysicsSpace().add(player);
        
        
         
       
    }    
    
    
    private BitmapText afficherTexte(String txt)
    {
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Mangal.fnt");
        BitmapText helloText = new BitmapText(guiFont, false);
        helloText.setSize(guiFont.getCharSet().getRenderedSize());
        helloText.setText(txt);
        helloText.setLocalTranslation(500, helloText.getLineHeight(), 0);
        guiNode.attachChild(helloText);
        return helloText;
    }
    
     public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
    if (animName.equals("walk")&&!up) {
      channel.setAnim("idle");
      
    }
    else channel.setAnim("walk");
  }
   
     
     private ActionListener actionListener;
     
    
     
     
     private int verifyUpAnalog1=0; 
     private int verifyUpAnalog2=0; 
  public void onAnalog(String binding, float isPressed, float tpf) {
         turnCharacterDependentlyOnCam();
    if (binding.equals("Left")) {
      left = true;
      
    } else if (binding.equals("Right")) {
      right= true;
      
    } else if (binding.equals("Up")) {
      up = true;
      verifyUpAnalog1++;
   // sinbadPlayer.rotateUpTo(new Vector3f(player.getViewDirection().x,cam.getRotation().getY(),sinbadPlayer.getLocalRotation().getZ()));
      
    } else if (binding.equals("Down")) {
      down = true;
    } else if (binding.equals("Jump")) {
       player.jump(); 
    }
    
    
  // System.out.println("walk "+ isPressed +" "+ binding);
        if (up && !channel.getAnimationName().equals("walk")) {
          channel.setAnim("walk",0.5f);
          channel.setLoopMode(LoopMode.DontLoop);
        }
        if(!up) channel.setAnim("idle");
    }
  
     
   private float turned=0;
   public  void turnCharacterDependentlyOnCam()
   { if(turned==0) turned=walkDirection.y;
       System.out.println("walkDirection = "+cam.getRotation().getY()+"  "+sinbadPlayer.getLocalRotation().getY());
       Quaternion q=sinbadPlayer.getLocalRotation();
       sinbadPlayer.setLocalRotation(new Quaternion(q.getX(),cam.getRotation().getY(),q.getZ(),cam.getRotation().getW()));
    //  sinbadPlayer.rotate(0, ((int)walkDirection.y)*30, 0); 
  }
     
    
   
    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
        // cam.setLocation(new Vector3f(0, 10, 0) );
        // if(!up) channel.setAnim("idle");
         
         camDir.set(cam.getDirection()).multLocal(0.6f);
        camLeft.set(cam.getLeft()).multLocal(0.4f);
        walkDirection.set(0, 0, 0);
       
        if (left) {
            walkDirection.addLocal(camLeft);
            left=false;
        
                  }
        if (right) {
            walkDirection.addLocal(camLeft.negate());
            right=false;
        }
        if (up) {
            walkDirection.addLocal(camDir);
           
            if(verifyUpAnalog1 ==verifyUpAnalog2){up=false; verifyUpAnalog1 =verifyUpAnalog2=0; }
            else
                verifyUpAnalog2=verifyUpAnalog1;
        }
        if (down) {
            walkDirection.addLocal(camDir.negate());
            down=false;
        }
        
       player.setWalkDirection(walkDirection);
        
     
        /*cam.setLocation(new Vector3f(player.getPhysicsLocation().x,
                player.getPhysicsLocation().y+15,
                player.getPhysicsLocation().z
                )); */
        
      /// modifaction de la la position du player 
        
        sinbadPlayer.setLocalTranslation(new Vector3f(
        player.getPhysicsLocation().x, player.getPhysicsLocation().y-2.5f,
        player.getPhysicsLocation().z));
        
      // System.out.println(walkDirection.toString());
      
        
       
         
    }
    
    /** We over-write some navigational key mappings here, so we can
   * add physics-controlled walking and jumping: */
  private void setUpKeys() {
    inputManager.addMapping("Left", new  KeyTrigger(KeyInput.KEY_LEFT));
    inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_RIGHT));
    inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_UP));
    inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_DOWN));
    inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
    inputManager.addMapping("addObject", new KeyTrigger(KeyInput.KEY_X));
    
   inputManager.addMapping("LClick", new MouseButtonTrigger(0));         // Left-button click
   inputManager.addMapping("RClick", new MouseButtonTrigger(1));         // Right-button click

    
    
    inputManager.addListener(this, "Left");
    inputManager.addListener(this, "Right");
    inputManager.addListener(this, "Up");
    inputManager.addListener(this, "Down");
    inputManager.addListener(this, "Jump");
    inputManager.addListener(actionListener,"LClick");
    inputManager.addListener(actionListener, "addObject");
  }
    
    
    
    
    
     private void initLight()
    {
           /** A white ambient light source. */ 
    AmbientLight ambient = new AmbientLight();
    ambient.setColor(ColorRGBA.White);
   rootNode.addLight(ambient); 
    
    DirectionalLight dl = new DirectionalLight();
    dl.setColor(ColorRGBA.White);
    dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
    rootNode.addLight(dl);
    }
     
     
   private void initScene()
   {
     sceneModel=assetManager.loadModel("Scenes/scene1.j3o");
       
       
     //  assetManager.registerLocator("newScene.zip", ZipLocator.class);
   // sceneModel = assetManager.loadModel("Models/terrain2/terrain2.j3o"); sceneModel.setLocalScale(10f, 2f, 10f);
    
    rootNode.attachChild(sceneModel);
     
       
    sceneModel.setLocalTranslation(0, 2, 0);
    
      //initTerrain();
    //initSimpleWater();
     //initPPcWater();
    
     CollisionShape sceneShape =
        CollisionShapeFactory.createMeshShape((Node)sceneModel);
        landscape = new RigidBodyControl(sceneShape, 0);
        sceneModel.addControl(landscape);  //define the scene as a rigid body
        
        bulletAppState.getPhysicsSpace().add(landscape);
              
       
    
   }
   Geometry g=null;
private Geometry putShape(Mesh shape, ColorRGBA color){
  g = new Geometry("coordinate axis", shape);
  Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
  mat.getAdditionalRenderState().setWireframe(true);
  mat.setColor("Color", color);
  g.setMaterial(mat);
  rootNode.attachChild(g);
  return g;
}

private void removeArrow()
{    if(g!=null)
    rootNode.detachChild(g);
}
    @Override
    public void simpleRender(RenderManager rm) {
        
        
        //TODO: add render code
    }

   

    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
    
    
    
    
    
    /// ABOUT WATER
   public void initSimpleWater(){ 
       SimpleWaterProcessor waterProcessor = new SimpleWaterProcessor(assetManager);
       
    waterProcessor.setReflectionScene(sceneModel); 
    Vector3f waterLocation=new Vector3f(0,-6,0); 
    waterProcessor.setPlane(new Plane(Vector3f.UNIT_Y, waterLocation.dot(Vector3f.UNIT_Y)));
    viewPort.addProcessor(waterProcessor); waterProcessor.setWaterDepth(10); // transparency of water 
    waterProcessor.setDistortionScale(0.07f); // strength of waves 
    waterProcessor.setWaveSpeed(0.05f); // speed of waves Quad 
    quad = new Quad(800,800); 
    quad.scaleTextureCoordinates(new Vector2f(6f,6f)); 
    Geometry water=new Geometry("water", quad); 
    water.setLocalRotation(new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_X)); 
    water.setLocalTranslation(-400, 0.32f, 400); 
    water.setShadowMode(RenderQueue.ShadowMode.Receive); 
    water.setMaterial(waterProcessor.getMaterial()); 
    rootNode.attachChild(water); } 
   
   public void initPPcWater(){ 
       FilterPostProcessor fpp = new FilterPostProcessor(assetManager); 
       water = new WaterFilter(rootNode, lightDirection); 
       water.setCenter(Vector3f.ZERO); 
       water.setRadius(2600); 
       water.setWaveScale(0.003f); 
       water.setMaxAmplitude(2f); 
       water.setFoamExistence(new Vector3f(1f, 4f, 0.5f)); 
       water.setFoamTexture((Texture2D) assetManager.loadTexture("Common/MatDefs/Water/Textures/foam2.jpg")); 
       water.setRefractionStrength(0.2f); 
       water.setWaterHeight(0.002f); fpp.addFilter(water); 
       viewPort.addProcessor(fpp); }
    
   
   
   
   private TerrainQuad terrain;
  Material mat_terrain;
   
   private void initTerrain() // essaye de charger un terrain à partir d'un height map
   {
       /** 1. Create terrain material and load four textures into it. */
    mat_terrain = new Material(assetManager, 
            "Common/MatDefs/Terrain/Terrain.j3md");
 
    /** 1.1) Add ALPHA map (for red-blue-green coded splat textures) */
    mat_terrain.setTexture("Alpha", assetManager.loadTexture(
            "Textures/Terrain/splat/alphamap.png"));
 
    /** 1.2) Add GRASS texture into the red layer (Tex1). */
    Texture grass = assetManager.loadTexture(
            "Textures/Terrain/splat/dirt.jpg");//grass
    grass.setWrap(WrapMode.Repeat);
    mat_terrain.setTexture("Tex1", grass);
    mat_terrain.setFloat("Tex1Scale", 64f);
 
    /** 1.3) Add DIRT texture into the green layer (Tex2) */
    Texture dirt = assetManager.loadTexture(
            "Textures/Terrain/splat/dirt.jpg");
    dirt.setWrap(WrapMode.Repeat);
    mat_terrain.setTexture("Tex2", dirt);
    mat_terrain.setFloat("Tex2Scale", 32f);
 
    /** 1.4) Add ROAD texture into the blue layer (Tex3) */
    Texture rock = assetManager.loadTexture(
            "Textures/Terrain/splat/dirt.jpg");//road
    rock.setWrap(WrapMode.Repeat);
    mat_terrain.setTexture("Tex3", rock);
    mat_terrain.setFloat("Tex3Scale", 128f);
 
    /** 2. Create the height map */
    AbstractHeightMap heightmap = null;
    Texture heightMapImage = assetManager.loadTexture(
            "Textures/Terrain/splat/scene4.png");
    heightmap = new ImageBasedHeightMap(heightMapImage.getImage());
    heightmap.load();
 
    /** 3. We have prepared material and heightmap. 
     * Now we create the actual terrain:
     * 3.1) Create a TerrainQuad and name it "my terrain".
     * 3.2) A good value for terrain tiles is 64x64 -- so we supply 64+1=65.
     * 3.3) We prepared a heightmap of size 512x512 -- so we supply 512+1=513.
     * 3.4) As LOD step scale we supply Vector3f(1,1,1).
     * 3.5) We supply the prepared heightmap itself.
     */
    int patchSize = 65;
    terrain = new TerrainQuad("my terrain", patchSize, 513, heightmap.getHeightMap());
 
    /** 4. We give the terrain its material, position & scale it, and attach it. */
    terrain.setMaterial(mat_terrain);
    terrain.setLocalTranslation(0, -100, 0);
    terrain.setLocalScale(2f, 1f, 2f);
    rootNode.attachChild(terrain);
    sceneModel=terrain;
   }
           
   public void initNiftyGUI()
   {
        
       Variables.setMain(this);
    NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
            assetManager, inputManager, audioRenderer, guiViewPort);
    Nifty nifty = niftyDisplay.getNifty();
    guiViewPort.addProcessor(niftyDisplay);
   // flyCam.setDragToRotate(true);
    
   
     /**
     * Åctivate the Nifty-JME integration: 
     */
    
    //nifty.setDebugOptionPanelColors(true);
    nifty.fromXml("Interface/int.xml", "start");
    
    //nifty.fromXml("Interface/tutorial/screen2.xml", "hud");
    
   
   }
   public void saySomething()
   {
       System.out.println("hello this is me");
   }
   
@NiftyEventSubscriber(id="connect")
public void onClick(String id, NiftyMousePrimaryClickedEvent event) {
 System.out.println("element with id [" + id + "] "
         + "clicked at [" + event.getMouseX() +
", " + event.getMouseY() + "]");
}

}