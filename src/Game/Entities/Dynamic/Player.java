package Game.Entities.Dynamic;

import Main.Handler;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

import Game.Entities.Static.Apple;
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
	public int appleTimer;
	public Boolean speedEater;
	public Boolean speedDecider;
	public Boolean pause;
	public Boolean addTailBugger;

	public double score;
	public double scoreAuxiliar;

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
		speedEater = false;
		appleTimer = 0;
		scoreAuxiliar = 0;
		addTailBugger = false;
	}

	public void tick(){
		moveCounter++;

		if(moveCounter>=speedSet) {
			checkCollisionAndMove();
			moveCounter=0;
			appleTimer++;
		}
		if(appleTimer > 200) {
			handler.getWorld().apple.isGood();
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
			addTailBugger = true;
			tailMod();
		}
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_EQUALS)) {
			speedDecider = true;
			speedChangerBug();
		}
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_MINUS)) {
			speedDecider= false;
			speedChangerBug();
		}
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ESCAPE)) {
			State.setState(handler.getGame().pauseState);
		}
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_R)) {
			restart();
		}
		
	}
	private void restart() {
		State.setState(handler.getGame().menuState);
		
	}

	public void speedChangerBug() {

		if(speedDecider) {
			speedSet--;
		}else {
			speedSet++;
		}

		if(speedSet < 0) {
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
				handler.getWorld().playerLocation[xCoord=handler.getWorld().GridWidthHeightPixelCount-1][yCoord]=true;
			}else{
				xCoord--;
			}
			break;
		case "Right":
			if(xCoord==handler.getWorld().GridWidthHeightPixelCount-1){
				handler.getWorld().playerLocation[xCoord=0][yCoord]=true;
			}else{
				xCoord++;
			}
			break;
		case "Up":
			if(yCoord==0){
				handler.getWorld().playerLocation[xCoord][yCoord=handler.getWorld().GridWidthHeightPixelCount-1]=true;
			}else{
				yCoord--;
			}
			break;
		case "Down":
			if(yCoord==handler.getWorld().GridWidthHeightPixelCount-1){
				handler.getWorld().playerLocation[xCoord][yCoord=0]=true;
			}else{
				yCoord++;
			}
			break;
		}
		handler.getWorld().playerLocation[xCoord][yCoord]=true;


		if(handler.getWorld().appleLocation[xCoord][yCoord]){
			Eat();
			scoreMod();
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

				if(playeLocation[i][j]){
					g.fillRect((i*handler.getWorld().GridPixelsize),
							(j*handler.getWorld().GridPixelsize),
							handler.getWorld().GridPixelsize,
							handler.getWorld().GridPixelsize);
				}

				if(handler.getWorld().appleLocation[i][j]) {
					g.setColor(new Color(247,23,23));
					g.fillRect((i*handler.getWorld().GridPixelsize),
							(j*handler.getWorld().GridPixelsize),
							handler.getWorld().GridPixelsize,
							handler.getWorld().GridPixelsize);
					if(!handler.getWorld().apple.isGood()) {
						g.setColor(new Color(212,232,31));
						g.fillRect((i*handler.getWorld().GridPixelsize),
								(j*handler.getWorld().GridPixelsize),
								handler.getWorld().GridPixelsize,
								handler.getWorld().GridPixelsize);
					}
				}

			}
		}
		g.setColor(Color.BLACK);
		//This piece here implements the SCORE.
		g.setFont(new Font("Times New Roman", Font.BOLD, 20));
		g.drawString("SCORE:", 10, 20);		
		g.drawString(Double.toString(score), 85, 20);

	}
	public void tailMod(){

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
		if(handler.getWorld().apple.isGood() || addTailBugger) {
			addTailBugger = false;
			lenght++;
			handler.getWorld().body.addLast(tail);
			handler.getWorld().playerLocation[tail.x][tail.y] = false;
		}else if (lenght > 1){
			lenght --;

			handler.getWorld().body.removeLast();

			for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
				for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {

					handler.getWorld().playerLocation[i][j]=false;

				}
			}
			//handler.getWorld().playerLocation[tail.x][tail.y] = true;
		}

	}

	public void scoreMod() {
		scoreAuxiliar = score -Math.round(Math.sqrt(2*score+1));
		if(handler.getWorld().apple.isGood()) {
			score += Math.round(Math.sqrt(2*score+1));
		}else {
			if(scoreAuxiliar <=0) {
				score = 0;
				scoreAuxiliar = 0;


			}else {
				score = score - Math.round(Math.sqrt(2*score+1));


			}
		}
		appleTimer = 0;
	}
	public void Eat(){
		handler.getWorld().appleLocation[xCoord][yCoord]=false;
		handler.getWorld().appleOnBoard=false;
		if(speedSet<0) {
			speedSet = 0;
		}else {
			speedSet -= 3;
		}
		tailMod();

	}

	public void kill(){
		lenght = 0;
		State.setState(handler.getGame().gameOver);

	}

	public boolean isJustAte() { 
		return justAte;
	}

	public void setJustAte(boolean justAte) {
		this.justAte = justAte;
	}
}
