package Game.Entities.Dynamic;

import Main.Handler;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

import Game.GameStates.State;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class Player {

	public int lenght;
	public boolean justAte;
	private Handler handler;

	public int xCoord;
	public int yCoord;

	public int moveCounter;
	public int speedSet;
	public Boolean speedDecider;
	public Boolean pause;

	public double score;

	public String direction;//is your first name one?

	public Player(Handler handler){
		this.handler = handler;
		xCoord = 0;
		yCoord = 0;
		moveCounter = 0;
		direction= "Right";
		justAte = false;
		lenght= 1;
		speedSet = 5;
		score = 0;
	}

	public void tick(){
		moveCounter++;

		//VVV SPEED CHANGE OVER HERE VVV
		if(moveCounter>=speedSet) {
			checkCollisionAndMove();
			moveCounter=0;
		}
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP)){
			if(direction == "Down") {
				direction = "Down";
			}else {
				direction="Up";
			}
		}if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN)){
			if(direction == "Up") {
				direction = "Up";
			}else {
				direction="Down";
			}
		}if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_LEFT)){
			if(direction == "Right") {
				direction = "Right";
			}else {
				direction="Left";
			}
		}if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_RIGHT)){
			if(direction == "Left") {
				direction = "Left";
			}else {
				direction="Right";
			}
		}
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_N)){
			addTail();
		}
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_EQUALS)) {
			speedDecider = true;
			speedChanger();
		}
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_MINUS)) {
			speedDecider= false;
			speedChanger();
		}
		//This if statement pauses the game
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ESCAPE)) {
			State.setState(handler.getGame().pauseState);
		}
	}
	public void speedChanger() {
		if(speedDecider) {
			speedSet--;
		}else {
			speedSet++;
		}if(speedSet < 0) {
			speedSet = 0;
		}
	}
	public void checkCollisionAndMove(){
		handler.getWorld().playerLocation[xCoord][yCoord]=false;
		int x = xCoord;
		int y = yCoord;
		switch (direction){
		case "Left":
			if(xCoord==0){
				kill();
			}else{
				xCoord--;
			}
			break;
		case "Right":
			if(xCoord==handler.getWorld().GridWidthHeightPixelCount-1){
				kill();
			}else{
				xCoord++;
			}
			break;
		case "Up":
			if(yCoord==0){
				kill();
			}else{
				yCoord--;
			}
			break;
		case "Down":
			if(yCoord==handler.getWorld().GridWidthHeightPixelCount-1){
				kill();
			}else{
				yCoord++;
			}
			break;
		}
		handler.getWorld().playerLocation[xCoord][yCoord]=true;


		if(handler.getWorld().appleLocation[xCoord][yCoord]){
			Eat();
		}

		if(!handler.getWorld().body.isEmpty()) {
			handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body.getLast().y] = false;
			handler.getWorld().body.removeLast();
			handler.getWorld().body.addFirst(new Tail(x, y,handler));
		}

	}

	public void render(Graphics g,Boolean[][] playeLocation){
		Random r = new Random();
		for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
			for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {
				g.setColor(Color.GREEN);

				if(playeLocation[i][j]||handler.getWorld().appleLocation[i][j]){
					g.fillRect((i*handler.getWorld().GridPixelsize),
							(j*handler.getWorld().GridPixelsize),
							handler.getWorld().GridPixelsize,
							handler.getWorld().GridPixelsize);
				}

			}
		}
		//This piece here implements the SCORE.
		g.drawString("SCORE", 10, 20);		
		g.drawString(Double.toString(score), 55, 20);

	}
	public void addTail(){
		score += Math.sqrt(2*score+1);
		lenght++;
		Tail tail= null;
		
		switch (direction){
		case "Left":
			if( handler.getWorld().body.isEmpty()){
				if(this.xCoord!=handler.getWorld().GridWidthHeightPixelCount-1){
					tail = new Tail(this.xCoord+1,this.yCoord,handler);
				}else{
					if(this.yCoord!=0){
						tail = new Tail(this.xCoord,this.yCoord-1,handler);
					}else{
						tail =new Tail(this.xCoord,this.yCoord+1,handler);
					}
				}
			}else{
				if(handler.getWorld().body.getLast().x!=handler.getWorld().GridWidthHeightPixelCount-1){
					tail=new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler);
				}else{
					if(handler.getWorld().body.getLast().y!=0){
						tail=new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler);
					}else{
						tail=new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler);

					}
				}

			}
			break;
		case "Right":
			if( handler.getWorld().body.isEmpty()){
				if(this.xCoord!=0){
					tail=new Tail(this.xCoord-1,this.yCoord,handler);
				}else{
					if(this.yCoord!=0){
						tail=new Tail(this.xCoord,this.yCoord-1,handler);
					}else{
						tail=new Tail(this.xCoord,this.yCoord+1,handler);
					}
				}
			}else{
				if(handler.getWorld().body.getLast().x!=0){
					tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
				}else{
					if(handler.getWorld().body.getLast().y!=0){
						tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));
					}else{
						tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));
					}
				}

			}
			break;
		case "Up":
			if( handler.getWorld().body.isEmpty()){
				if(this.yCoord!=handler.getWorld().GridWidthHeightPixelCount-1){
					tail=(new Tail(this.xCoord,this.yCoord+1,handler));
				}else{
					if(this.xCoord!=0){
						tail=(new Tail(this.xCoord-1,this.yCoord,handler));
					}else{
						tail=(new Tail(this.xCoord+1,this.yCoord,handler));
					}
				}
			}else{
				if(handler.getWorld().body.getLast().y!=handler.getWorld().GridWidthHeightPixelCount-1){
					tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));
				}else{
					if(handler.getWorld().body.getLast().x!=0){
						tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
					}else{
						tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));
					}
				}

			}
			break;
		case "Down":
			if( handler.getWorld().body.isEmpty()){
				if(this.yCoord!=0){
					tail=(new Tail(this.xCoord,this.yCoord-1,handler));
				}else{
					if(this.xCoord!=0){
						tail=(new Tail(this.xCoord-1,this.yCoord,handler));
					}else{
						tail=(new Tail(this.xCoord+1,this.yCoord,handler));
					} System.out.println("Tu biscochito");
				}
			}else{
				if(handler.getWorld().body.getLast().y!=0){
					tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));
				}else{
					if(handler.getWorld().body.getLast().x!=0){
						tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
					}else{
						tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));
					}
				}

			}
			break;
		}
		handler.getWorld().body.addLast(tail);
		handler.getWorld().playerLocation[tail.x][tail.y] = true;
		
		

	}

	public void Eat(){
		handler.getWorld().appleLocation[xCoord][yCoord]=false;
		handler.getWorld().appleOnBoard=false;
		addTail();
		
	}

	public void kill(){
		lenght = 0;
		for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
			for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {

				handler.getWorld().playerLocation[i][j]=false;

			}
		}
	}

	public boolean isJustAte() { 
		return justAte;
	}

	public void setJustAte(boolean justAte) {
		this.justAte = justAte;
	}
}
