package model;


public class ChessPiece {
    // the owner of the chess
    private PlayerColor owner;

    // Elephant? Cat? Dog? ...
    private String name;
    private int rank;
    private final int RANK;

    public ChessPiece(PlayerColor owner, String name, int rank) {
        this.owner = owner;
        this.name = name;
        this.rank = rank;
        RANK = rank;
    }

    public void setRank(int R){
        if(R >= 0 && R <= 8){
            rank = R;
        }
    }
    public int getRANK(){
        return RANK;
    }

    public boolean canCapture(ChessPiece target) {
        // TODO: Finish this method!//已写，待查验
        if(!target.owner.equals(this.owner)) {
            if(target.rank <= this.rank){
                if(target.rank==1&&this.rank==8){return false;}else{return true;}
            }else if(target.rank==8&&this.rank==1){return true;}return false;
        }else{return false;}
    }

    public String getName() {
        return name;
    }

    public PlayerColor getOwner() {
        return owner;
    }
    public String getOwnerString(){
        if(owner.equals(PlayerColor.BLUE)){
            return "BLUE";
        }else{
            return "RED";
        }
    }

    public int getRank(){return rank;}

    public String toString(){
        String aim = String.format("%s %s %s %s",this.owner,this.name,this.rank,this.RANK);
        return aim;
    }
}
