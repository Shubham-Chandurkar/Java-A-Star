import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DemoPanel extends JPanel {

    final int maxCol = 15;
    final int maxRow = 10;
    final int nodeSize = 70;
    final int screenWidth = nodeSize*maxCol;
    final int screenHeight = nodeSize*maxRow;

    Node[][] node = new Node[maxCol][maxRow];
    Node startNode, goalNode, currentNode;

    ArrayList<Node> openList = new ArrayList<>();
    ArrayList<Node> checkedList = new ArrayList<>();

    boolean goalReached = false;

    public DemoPanel(){
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.black);
        this.setLayout(new GridLayout(maxRow,maxCol));

        //placing node
        int col = 0;
        int row = 0;

        while(col<maxCol && row < maxRow){
            node[col][row] = new Node(col,row);
            this.add(node[col][row]);


            col++;
            if(col == maxCol){
                col=0;
                row++;
            }
        }
//        Node sNode = randomNode();
//        setStartNode(sNode.col,sNode.row);
//        Node gNode = randomNode();
//        setGoalNode(sNode.col,gNode.row);

        setStartNode(0,9);
        setGoalNode(14,3);
        setCostOnNode();

        //placing solid nodes
        for(int i =0;i<30;i++) {
            Node tNode = randomNode();
            if(tNode ==startNode || tNode ==goalNode){
                i--;
            }else{
                setSolidNode(tNode.col,tNode.row);
            }


        }

        search();

    }

    private void setStartNode(int col, int row){
        node[col][row].setAsStart();
        startNode = node[col][row];
        currentNode = startNode;
    }
    private void setGoalNode(int col, int row){
        node[col][row].setAsGoal();
        goalNode = node[col][row];
    }

    private void setSolidNode(int col, int row){
        node[col][row].setAsSolid();
        node[col][row].setEnabled(false);
    }

    private void setCostOnNode(){
        int col =0;
        int row =0;
        while(col<maxCol && row < maxRow){
                getCost(node[col][row]);
            col++;
            if(col == maxCol){
                col=0;
                row++;
            }
        }
    }

    private void getCost(Node node){

        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;


        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;

        node.fCost = node.gCost+node.hCost;

        if(node != startNode && node != goalNode){
            node.setText("<html> F: "+ node.fCost + "<br>" + "G: "+node.gCost+"</html>");
        }



    }

    private void search(){
        while(!goalReached){
            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.setAsChecked();
            checkedList.add(currentNode);
            openList.remove(currentNode);

            if(row-1 >=0)
                openNode(node[col][row-1]);
            if(col-1 >=0)
                openNode(node[col-1][row]);
            if(row+1 <maxRow)
                openNode(node[col][row+1]);
            if(col+1 <maxCol)
                openNode(node[col+1][row]);

            int bestNodeIndex = 0;
            int bestNodefCost = 999;

            for(int i = 0;i< openList.size();i++){
                if(openList.get(i).fCost < bestNodefCost){
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                }else if( openList.get(i).fCost == bestNodefCost){
                    if(openList.get(i).gCost < openList.get(bestNodeIndex).gCost){
                        bestNodeIndex = i;
                    }
                }
            }
                currentNode = openList.get(bestNodeIndex);
            if(currentNode == goalNode){
                goalReached = true;
                trackPath();
            }

        }
    }

    private void openNode(Node node){

        if(!node.open && !node.checked && !node.solid){
            node.setAsOpen();
            node.parent = currentNode;
            openList.add(node);
        }

    }

    private void trackPath(){
        Node current = goalNode;

        while (current!= startNode){
            current = current.parent;

            if(current!= startNode){
                current.setAsPath();
            }
        }
    }

    private Node randomNode(){
        int tempCol =(int) (Math.random() * (maxCol));
        int tempRow = (int) (Math.random() * (maxRow));
        return  node[tempCol][tempRow];
    }


}
