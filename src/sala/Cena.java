package sala;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT; //primitivas 3D

/**
 *
 * @author siabr
 */
public class Cena implements GLEventListener, KeyListener {

    private float angulo = 0;
    private GL2 gl;
    private GLU glu;
    private GLUT glut;
    private int tonalizacao = GL2.GL_SMOOTH;
    private boolean liga = false;

    @Override
    public void init(GLAutoDrawable drawable) {
        //dados iniciais da cena
        GL2 gl = drawable.getGL().getGL2();
        //habilita o buffer de profundidade
        gl.glEnable(GL2.GL_DEPTH_TEST);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        //obtem o contexto Opengl
        gl = drawable.getGL().getGL2();
        glut = new GLUT(); //objeto da biblioteca glut

        //define a cor da janela (R, G, G, alpha)
        gl.glClearColor(0, 0, 0, 1);
        //limpa a janela com a cor especificada
        //limpa o buffer de profundidade
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity(); //lê a matriz identidade

        /*
            desenho da cena        
        *
         */
        // criar a cena aqui....
        if (liga) {
            iluminacaoEspecular();
            ligaLuz();
        }

        gl.glRotatef(angulo, 0, 1, 0);

        //gl.glColor3f(1.0f, 1.0f, 1.0f); // branca
        gl.glScalef(0.7f, 0.7f, 0.7f);

        gl.glPushMatrix();

        gl.glRotatef(30, 0, 1, 1);
        desenho();
        gl.glPopMatrix();

//        gl.glColor3f(1.0f, 0.5f, 0.0f); // laranja
//        esfera();
        if (liga) {
            desligaluz();
        }

        ///// Exemplo de Texto        
        //gl.glColor3f(0, 0, 0);
        //desenhaTexto(gl, -95, 90, "Angulo: " + angulo);
        gl.glFlush();
    }

    public void desenho() {
        gl.glPushMatrix();
        paredes();
        mesa();
        abajur();
        gl.glPopMatrix();
    }

    public void desenhaTexto(GL2 gl, int x, int y, String frase) {
        glut = new GLUT(); //objeto da biblioteca glut
        gl.glRasterPos2f(x, y);
        glut.glutBitmapString(GLUT.BITMAP_8_BY_13, frase);
    }

    public void cilindro() {
        gl.glTranslatef(0, 0, -89);
        glut.glutSolidCylinder(3, 70, 30, 30);
    }

    public void desenhaCilindro(float x, float y) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, 0f);
        cilindro();
        gl.glPopMatrix();
    }

    public void mesa() {
        gl.glPushMatrix();
        gl.glRotatef(90f, -1, 0, 0);
        gl.glPushMatrix();
        tampaMesa();
        pesDaMesa();
        gl.glPopMatrix();
        gl.glPopMatrix();

    }

    public void pesDaMesa() {
        gl.glPushMatrix();
        gl.glColor3f(1, 0, 0); // branca chao
        desenhaCilindro(-40, -40);
        desenhaCilindro(40, -40);
        desenhaCilindro(-40, 40);
        desenhaCilindro(40, 40);
        gl.glPopMatrix();
    }

    public void tampaMesa() {
        gl.glPushMatrix();
        gl.glColor3f(1, 0, 0); // branca chao
        gl.glTranslatef(0, 0, -20);
        gl.glScalef(3.8f, 3.5f, 0.1f);
        cubo();
        gl.glPopMatrix();

    }

    public void cubo() {
        gl.glPushMatrix();
        glut.glutSolidCube(30);
        gl.glPopMatrix();
    }

    private void esfera() {
        glut.glutSolidSphere(60, 15, 15);
    }

    private void parede() {
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(-90.0f, 90.0f, -90.0f);
        gl.glVertex3f(90.0f, 90.0f, -90.0f);
        gl.glVertex3f(90.0f, -90.0f, -90.0f);
        gl.glVertex3f(-90.0f, -90.0f, -90.0f);
        gl.glEnd();
    }

    private void paredes() {

        gl.glPushMatrix();

        gl.glPushMatrix();
        gl.glColor3f(0, 0.5f, 1.0f); // azul parede
        parede();
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glRotatef(90f, -1, 0, 0);
        gl.glColor3f(0, 0.5f, 1.0f); // branca chao
        parede();
        gl.glPopMatrix();

        gl.glPopMatrix();

    }

    public void abajur() {
        gl.glPushMatrix();
        gl.glRotatef(90f, -1, 0, 0);
        gl.glPushMatrix();
        baseAbajur();
        gl.glPopMatrix();
        gl.glPopMatrix();

    }

    public void baseAbajur() {
        gl.glPushMatrix();
        cilindro();
        gl.glPopMatrix();
    }

    public void iluminacaoEspecular() {
        float luzAmbiente[] = {0.2f, 0.2f, 0.2f, 1.0f}; //cor
        float luzEspecular[] = {1.0f, 1.0f, 1.0f, 1.0f}; //cor
        float posicaoLuz[] = {-50.0f, 0.0f, 100.0f, 1.0f}; //pontual

        //intensidade da reflexao do material        
        int especMaterial = 128;
        //define a concentracao do brilho
        gl.glMateriali(GL2.GL_FRONT, GL2.GL_SHININESS, especMaterial);

        //define a reflectância do material
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, luzEspecular, 0);

        //define os parâmetros de luz de número 0 (zero)
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, luzAmbiente, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, luzEspecular, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, posicaoLuz, 0);
    }

    public void ligaLuz() {
        // habilita a definição da cor do material a partir da cor corrente
        gl.glEnable(GL2.GL_COLOR_MATERIAL);

        // habilita o uso da iluminação na cena
        gl.glEnable(GL2.GL_LIGHTING);
        // habilita a luz de número 0
        gl.glEnable(GL2.GL_LIGHT0);
        //Especifica o Modelo de tonalizacao a ser utilizado 
        //GL_FLAT -> modelo de tonalizacao flat 
        //GL_SMOOTH -> modelo de tonalização GOURAUD (default)        
        gl.glShadeModel(tonalizacao);
    }

    public void desligaluz() {
        //desabilita o ponto de luz
        gl.glDisable(GL2.GL_LIGHT0);
        //desliga a iluminacao
        gl.glDisable(GL2.GL_LIGHTING);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        //obtem o contexto grafico Opengl
        gl = drawable.getGL().getGL2();
        //ativa a matriz de projeção
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity(); //lê a matriz identidade
        //projeção ortogonal (xMin, xMax, yMin, yMax, zMin, zMax)
        gl.glOrtho(-100, 100, -100, 100, -100, 100);
        //ativa a matriz de modelagem
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        System.out.println("Reshape: " + width + ", " + height);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
            //........
        }
        switch (e.getKeyChar()) {
            case 'r':
                angulo += 15;
                break;
            case 't':
                tonalizacao = tonalizacao == GL2.GL_SMOOTH ? GL2.GL_FLAT : GL2.GL_SMOOTH;
                break;
            // liga / desliga luz
            case 'l':
                if (liga) {
                    liga = false;
                } else {
                    liga = true;
                }
                System.out.println(liga);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
