/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.system;

import com.google.common.base.Joiner;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.system.AppSettings;
import com.simsilica.es.EntityId;
import com.spectre.app.SimpleAppState;
import com.spectre.scene.visual.components.InScenePiece;
import com.spectre.systems.essence.EssenceSystem;
import com.spectre.systems.essence.components.EssencePiece;
import java.math.BigDecimal;

/**
 *
 * @author Kyle Williams
 */
public class TestEssenceSystem extends SimpleAppState {

    public static void main(String[] args) {
        Application app = new SimpleApplication(new TestEssenceSystem()) {
            @Override
            public void simpleInitApp() {
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        AppSettings settings = new AppSettings(true);
        settings.setRenderer(AppSettings.LWJGL_OPENGL2);
        settings.setAudioRenderer(AppSettings.LWJGL_OPENAL);
        settings.putBoolean("DebugMode", true);
        app.setSettings(settings);
        app.start();
    }
    private BitmapText[] labels;
    private BitmapFont guiFont;
    private EssenceSystem es;

    @Override
    public void SimpleAppState() {
        getStateManager().attach(es = new EssenceSystem());

        getStateManager().attach(new AbstractAppState() {
            private EntityId entityId;
            private Joiner joiner = Joiner.on("/n").skipNulls();
            private final StringBuilder stringBuilder = new StringBuilder();

            @Override
            public void initialize(AppStateManager stateManager, Application app) {
                super.initialize(stateManager, app);
                entityId = getEntityData().createEntity();
                getEntityData().setComponents(entityId, new EssencePiece(), new InScenePiece());

                guiFont = app.getAssetManager().loadFont("Interface/Fonts/TimesNewRoman12Bold.fnt");
                labels = new BitmapText[4];
                for (int i = 0; i < labels.length; i++) {
                    labels[i] = new BitmapText(guiFont, false);
                    labels[i].setLocalTranslation(getSettings().getWidth() / 2,
                            //labels[i].getLineHeight() * (i + 1), 
                            getSettings().getHeight() / 2 + labels[i].getLineHeight() * (i - 1),
                            0);
                    labels[i].setCullHint(CullHint.Never);
                    getGuiNode().attachChild(labels[i]);
                }
            }

            @Override
            public void update(float tpf) {
                getEssenceStats();
            }

            public void getEssenceStats() {
                String[] s = new String[4];
                s[0] = "Health:\t " + es.getHealth(entityId) + "/" + es.getBeginingHealth(entityId);
                s[1] = "Focus:\t " + es.getFocus(entityId) + "/" + es.getFocusLevel(entityId);
                double r = es.getCurrentReplenishRate(entityId);
                int decimalPlaces = 1;
                BigDecimal bd = new BigDecimal(r);
                bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP); // setScale is immutable
                r = bd.doubleValue();
                s[2] = "Rate:\t " + r + "/" + es.getReplenishRate(entityId);
                s[3] = "Increase Focus Rate On:\t" + es.isIncreaseFocusRate(entityId);
                for (int i = 0; i < labels.length; i++) {
                    labels[i].setText(s[i]);
                }
                //return joiner.join(health, focus, rate, other);

            }
        });
    }
}
