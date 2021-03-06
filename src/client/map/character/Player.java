/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.map.character;

import client.map.Map;
import client.map.Region;
import client.map.World;
import client.map.Zone;
import client.script.ScriptableMethod;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.nio.ByteBuffer;

import shared.constants.PckCode;
import shared.pck.Pck;
import shared.variables.Variables;

/**
 *
 * @author admin
 */
public class Player extends PlayableCharacter 
implements AnimEventListener {

    
    /**
	 * region ou se trouve le joueur actuellement
	 */
	private Region currentRegion = null;

	private Zone currentZone;

	private Map currentMap;


	private boolean is_scenarizing;

	private Group maingroup;

	//private Text debug;
	
	private boolean canMove = true;
    
    
    
    //private AnimChannel channel;
    private boolean left = false, right = false, up = false, down = false;
    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();

 

    public AnimControl getControl() {
        return control;
    }

    public void setControl(AnimControl control) {
        this.control = control;
    }

    public Vector3f getWalkDirection() {
        return walkDirection;
    }

    public void setWalkDirection(Vector3f walkDirection) {
        this.walkDirection = walkDirection;
    }
    private AnimControl control;
    private CharacterControl playerControl;

    public CharacterControl getplayerControl() {
        return playerControl;
    }

    public void setPlayer(CharacterControl player) {
        this.playerControl = player;
    }
    private Node playerModel;

    public Node getPlayerModel() {
        return playerModel;
    }

    public void setPlayerModel(Node playerModel) {
        this.playerModel = playerModel;
    }
    private Vector3f walkDirection = new Vector3f();

    public Player(World world, String login) {
        super(world, login);
          //     initPlayer();
       //  Variables.getLaGame().getPhysicsSpace().addCollisionListener(this);
    }
    
  
    private CapsuleCollisionShape capsuleShape;
CollisionShape pShape;
    public void initPlayer() {
        System.out.println("Player ->initPlayer() : !!");
      //  playerModel = (Node) Variables.getLaGame().getAssetManager().loadModel("Models/high-sinbad/Sinbad.mesh.xml");
      //  playerModel.setLocalScale(0.5f);
        
        
      //  playerModel=Variables.getMainPlayer().getGraphic();
      //  playerModel.move(10, 5, -10);

      //  control = playerModel.getControl(AnimControl.class);
//        control.addListener((AnimEventListener) this);
  //      channel = control.createChannel();
    //    channel.setAnim("idle");
        super.startAnimation(CharacterAnimation.idle);
        //super.startAnimation(super.CharacterAnimation.idle);
        //startAnimation
        capsuleShape =
                new CapsuleCollisionShape(2f, 1f, 1);
        if(Variables.getMainPlayer().getGraphic()!=null)
 pShape =CollisionShapeFactory.createMeshShape((Node) Variables.getMainPlayer().getGraphic());
        else 
            System.out.println("pShape=null!!!");

        playerControl = new CharacterControl(capsuleShape, 0.1f);
        playerControl.setJumpSpeed(30);
        playerControl.setFallSpeed(600);
        playerControl.setGravity(50);
      //  playerControl.setCcdMotionThreshold(getCcdMotionThreshold());

        playerControl.setPhysicsLocation(new Vector3f(-10, 5, -10));
//        playerModel.addControl(control);
        //playerModel.setName("playerModel");


        Variables.getLaGame().getBulletAppState().getPhysicsSpace().add(playerControl);
       


    }

    public void attachToScene() {
        playerModel=getGraphic();
        if(playerModel!=null)
        Variables.getLaGame().getRootNode().attachChild(playerModel);
        else
        System.out.println("Player -> attachToScene() : getGraphic retourne nulle !!!");
        

    }
    //private float turned=0;

    public void turnCharacterDependentlyOnCam() {
        System.out.println("this is turn character");
        //if(turned==0) turned=walkDirection.y;
        System.out.println("walkDirection = " + Variables.getCam().getRotation().getY() + "  " + playerModel.getLocalRotation().getY());
        Quaternion q = playerModel.getLocalRotation();
        playerModel.setLocalRotation(new Quaternion(q.getX(), Variables.getCam().getRotation().getY(), q.getZ(), Variables.getCam().getRotation().getW()));
        //playerModel.rotate(0, 10, 0); 
        // Variables.getLaGame().getRootNode().getChild("playerModel").rotate(0, 10, 0); 
        playerModel.updateGeometricState();
    }

    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
    //    System.out.println("this is on animcycleDone");

        if (animName.equals("walk") && !up && moving!=Moving.target && moving!=Moving.directionnal) {
            channel.setAnim("idle");
        } else if (animName.equals("walk")) {
            channel.setAnim("walk");
        }
      
    }

    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
    }
    private int verifyUpAnalog1 = 0;
    private int verifyUpAnalog2 = 0;

    public void freeMovePlayer(String binding) {
        turnCharacterDependentlyOnCam();
        if (binding.equals("Left")) {
            left = true;

        } else if (binding.equals("Right")) {
            right = true;

        } else if (binding.equals("Up") || moving==Moving.target) {
            up = true;
            verifyUpAnalog1++;
            // sinbadPlayer.rotateUpTo(new Vector3f(player.getViewDirection().x,cam.getRotation().getY(),sinbadPlayer.getLocalRotation().getZ()));

        } else if (binding.equals("Down")) {
            down = true;
        } else if (binding.equals("Jump")) {

            playerControl.jump();
        }

        // System.out.println("walk "+ isPressed +" "+ binding);
      //  if ((up || moving==Moving.target) && !channel.getAnimationName().equals("walk")) {
           // channel.setAnim("walk", 0.5f);
           // channel.setLoopMode(LoopMode.Loop);
            
        //}
       

    }
    Vector3f lastWalkDirection = new Vector3f(0, 0, 0);
    Vector3f nullVector = new Vector3f(0, 0, 0);
    //private Moving moving = Moving.stop;
    private Vector3f target;

    public Vector3f getTarget() {
        return target;
    }

    public void setTarget(Vector3f target) {
        this.target = target;
       
    }

    public void moveTo(Vector3f to) {
       

        target = new Vector3f(to);
        moving = Moving.target;
    }

    /*
     * etteinte du flèche de positionnement
     */
    public void endMoving() {
        moving = Moving.stop;
        //channel.setAnim("idle");
        
        super.startAnimation(CharacterAnimation.idle);
        Variables.getMoveCursor().removeArrow();
    }
    
boolean lastStateIsStop=true;
    public void update() {
        //System.out.println("this is update from the player class");

        camDir.set(Variables.getCam().getDirection()).multLocal(0.6f);
        camLeft.set(Variables.getCam().getLeft()).multLocal(0.4f);
        walkDirection.set(0, 0, 0);
        float a=Variables.getCam().getRotation().getY();

        if(!(up||left||right||down|| moving==Moving.target|| moving==Moving.stop || lastStateIsStop))
        {
            System.out.println("stop called !!");
            stop();
            lastStateIsStop=true;
        }

        if (left) {
            walkDirection.addLocal(camLeft);
            moveDirectionnal(a+FastMath.HALF_PI);
            left = false;
         if(lastStateIsStop)   lastStateIsStop=false;
        }
        if (right) {
            walkDirection.addLocal(camLeft.negate());
            moveDirectionnal(a-FastMath.HALF_PI);
            right = false;
         if(lastStateIsStop)   lastStateIsStop=false;
        }
        if (up) {
            System.out.println("this is up !!");
            walkDirection.addLocal(camDir);
          if(lastStateIsStop)  moveDirectionnal(a);
            lastStateIsStop=false;
            if (verifyUpAnalog1 == verifyUpAnalog2) {
                up = false;
                verifyUpAnalog1 = verifyUpAnalog2 = 0;
            } else {
                verifyUpAnalog2 = verifyUpAnalog1;
            }
        }
        if (down) {
            walkDirection.addLocal(camDir.negate());
           if(lastStateIsStop) moveDirectionnal(a);
            lastStateIsStop=false;
            down = false;
        }


        playerControl.setWalkDirection(walkDirection);

        if (walkDirection.equals(nullVector)) {
            playerControl.setViewDirection(new Vector3f(lastWalkDirection.x, 0, lastWalkDirection.z));
        } else {

            //System.err.println("walkDirection= "+walkDirection.toString());
            playerControl.setViewDirection(new Vector3f(walkDirection.x, 0, walkDirection.z));
            lastWalkDirection = new Vector3f(walkDirection.x, walkDirection.y, walkDirection.z);
        }

        if (moving==Moving.target) {
            playerControl.setViewDirection(target);
            playerControl.setWalkDirection(target);
        }

        
        CollisionResults results = new CollisionResults();
        if (Variables.getMoveCursor() != null && Variables.getMoveCursor().getNodeGostCursor() != null && playerModel!= null) {
            playerModel.collideWith(Variables.getMoveCursor().getNodeGostCursor().getWorldBound(), results);
        }

        // Use the results
        if (results.size() > 0) {
         
            CollisionResult closest = results.getClosestCollision();
            
            endMoving();
        }
    }
    /*  
     * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
     * updated for the v.2014 by Hamza ABED  @2014
     */

    public void moveTo(float x, float z) {
        //this.moving = Moving.target;
        //moveAnimation();
        moving = Moving.target;


       // channel.setAnim("walk", 0.5f);
       // channel.setLoopMode(LoopMode.Loop);

        super.startAnimation(CharacterAnimation.walk);
        Vector3f o = playerModel.getLocalTranslation();

        Vector3f t = new Vector3f(x, o.y, z);
        t.subtractLocal(o).normalizeLocal();

        Matrix3f m = new Matrix3f();
        m.fromStartEndVectors(Vector3f.UNIT_Z, t);

        Quaternion q = new Quaternion().fromRotationMatrix(m);
        Quaternion q2 = new Quaternion().fromRotationMatrix(m.invert());

        // correction d'un bug survenant quand la camera est dans l'axe des Z
        if (q.getX() > 0.5f) {
            q.set(0, q.getX(), q.getZ(), q.getW());
            //q.y = q.x;
            //q.x = 0;
            q2.set(0, q2.getX(), q2.getZ(), q2.getW());
            //q2.y = q2.x;
            //q2.x = 0;	
        }
//this is about rotation

        playerModel.setLocalRotation(q);
        target = q.getRotationColumn(2);

        /*
         if (characterNode != null) {
         characterNode.setLocalRotation(q);
         onHead.setLocalRotation(q2);
         }
         */
    }
/**
	 * @return renvoie le group principal
	 */
    public Group getMainGroup() {
		if (maingroup==null&&groups.size()>0)
				return groups.get(0);
		return maingroup;
	}

    @Override
    protected boolean canMoveAt(Vector3f newPos) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected boolean testCollision(Vector3f newPos, Vector3f dir) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /* ********************************************************** *
	 * * 				Mise à jour	- Divers					* *
	 * ********************************************************** */
	/* (non-Javadoc)
	 * @see client.map.character.PlayableCharacter#updateRights()
	 */
	
	protected void updateRights() {
		super.updateRights();
		Variables.getHud().updateRights(isAdmin());
	}

	/* (non-Javadoc)
	 * @see client.map.character.PlayableCharacter#receiveCommitPck(java.nio.ByteBuffer)
	 */
	
	public void receiveCommitPck(ByteBuffer message) {
		boolean update = x<0 || z<0;
		super.receiveCommitPck(message);
		if (update) {
			System.out.println("first map @ "+getGraphicPosition());
			checkZones();
			mapUpdate();
		}
	}
        
        
        
        /**
	 * Test si le joueur change de region et le cas échéant demande au 
	 * serveur de se mettre à jour
	 */
	private void checkZones() {
		Vector3f v = getGraphicPosition();
		if (v == null) return;

		Zone newZone = world.getZone(v.x, v.z);
		if(newZone != currentZone) {
			Variables.getChatSystem().debug(currentZone+" > "+newZone);
			currentZone = newZone;
			Pck pck = new Pck(PckCode.PLAYER_REQUEST_ZONE_UPDATE);
			pck.putFloat(v.x,v.z);
			Variables.getClientConnecteur().send(pck);
		}
	}
        
        /***********************************************************
         *               Deplacement
         ***********************************************************/
        
        /* (non-Javadoc)
	 * @see client.map.character.AbstractCharacter#moveDirectionnal(float)
	 */
	@Override
	public void moveDirectionnal(float alpha) {
		if (characterNode != null && canMove()) {
			Pck pck = new Pck(PckCode.PLAYER_START_MOVE);
			pck.putString(getKey());
			pck.putFloat(characterNode.getLocalTranslation().x, characterNode.getLocalTranslation().z);
			pck.putEnum(Moving.directionnal);
			pck.putFloat(alpha);
			pck.putBoolean(walk);
			Variables.getClientConnecteur().send(pck);

                super.moveDirectionnal(alpha);
                }
	}
        /**
	 * permet d'arreter le deplacement du joueur
	 */
	@ScriptableMethod
	public void stop() {
            System.err.println("this is STOP!!");
            if (moving != Moving.stop) {
                super.endMoveAt(getGraphic().getLocalTranslation().x,
                        getGraphic().getLocalTranslation().z);
            }
	}
        
        /*
	 * (non-Javadoc)
	 * 
	 * @see client.character.PlayableCharacter#endMove()
	 */
	@Override
	protected void endMove() {
		super.endMove();
		Pck pck = new Pck(PckCode.PLAYER_END_MOVE);
		pck.putString(getKey());
		pck.putFloat(x, z);
		Variables.getClientConnecteur().send(pck);
		//world.getGame().getUserInterface3D().getMoveCursor().hide();
	}
        /**
	 * @param canMove the canMove to set
	 */
	@ScriptableMethod(description="permet au joueur de se déplacer ou non")
	public void setCanMove(boolean canMove) {
		this.canMove = canMove;
	}

	/**
	 * indique si le joueur peu bouger
	 */
	@ScriptableMethod(description="indique si le joueur peu se déplacer ou non")
	public boolean canMove() {
		return canMove;
	}
        
        /*
	 * (non-Javadoc)
	 * 
	 * @see client.character.AbstractCharacter#isPlayer()
	 */
	@Override
	public boolean isPlayer() {
		return true;
	}
        
         /* ********************************************************** *
	 * * 				Gestion de la map courante				* *
	 * ********************************************************** */

	/**
	 * met à jour la map courante
	 */
	public void mapUpdate() {
		Vector3f v = getGraphicPosition();
		Map map = world.getMapAt(v.x, v.z);
		if (currentMap != map) {
			Variables.getChatSystem().debug(currentMap+" > "+map);
			enter(map);
		}
	}	

	/**
	 * execute le scrip d'entrer de carte 
	 * @param map
	 */
	private void enter(Map map) {
		if (map != null) {
		System.err.println("Player-> EnterMap!!");
			currentMap = map;
		}

	}


    
}
