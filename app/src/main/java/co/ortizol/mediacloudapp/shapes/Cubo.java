package co.ortizol.mediacloudapp.shapes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import co.ortizol.mediacloudapp.R;

/**
 * Representa la configuración visual de un cubo.
 */
public class Cubo {

    private FloatBuffer vertexBuffer;
    private FloatBuffer texturaBuffer;

    int[] texturaIDs = new int[1];

    private float texturaCoord[] = {
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.0f, 0.0f,
            0.5f, 0.0f
    };

    private int numCaras = 6;

    private float[] vertices = {
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            -0.5f, 0.5f, 0.0f,
            0.5f, 0.5f, 0.0f
    };

    public Cubo() {
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());

        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        ByteBuffer tbb = ByteBuffer.allocateDirect(texturaCoord.length * 4);
        tbb.order(ByteOrder.nativeOrder());
        texturaBuffer = tbb.asFloatBuffer();
        texturaBuffer.put(texturaCoord);
        texturaBuffer.position(0);
    }

    public void dibujar(GL10 gl) {
        gl.glFrontFace(GL10.GL_CCW);
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glCullFace(GL10.GL_BACK);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texturaBuffer);

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0.0f, 0.5f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glRotatef(270.0f, 0.0f, 0.5f, 0.0f);
        gl.glTranslatef(0.0f, 0.f, 0.5f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glRotatef(180.0f, 0.0f, 0.5f, 0.0f);
        gl.glTranslatef(0.0f, 0.f, 0.5f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glRotatef(90.0f, 0.0f, 0.5f, 0.0f);
        gl.glTranslatef(0.0f, 0.f, 0.5f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glRotatef(270.0f, 0.5f, 0.0f, 0.0f);
        gl.glTranslatef(0.0f, 0.f, 0.5f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glRotatef(90.0f, 0.5f, 0.0f, 0.0f);
        gl.glTranslatef(0.0f, 0.f, 0.5f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        gl.glPopMatrix();

        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisable(GL10.GL_CULL_FACE);
    }

    public void cargarTextura(GL10 gl, Context context) {
        gl.glGenTextures(1, texturaIDs, 0);

        gl.glBindTexture(GL10.GL_TEXTURE_2D, texturaIDs[0]);

        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

        InputStream istream = context.getResources().openRawResource(R.raw.metal_texture);

        Bitmap bitmap;

        try {
            bitmap = BitmapFactory.decodeStream(istream);
        } finally {
            try {
                istream.close();
            } catch (IOException e) {
            }
        }

        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
    }

}
