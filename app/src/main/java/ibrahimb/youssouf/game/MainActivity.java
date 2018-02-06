package ibrahimb.youssouf.game;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    //Les variables
    int sommeL1;
    int sommeL2;
    int sommeL3;
    int sommeC1;
    int sommeC2;
    int sommeC3;
    int[] numbersAllowed = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    int[] tabGAME = new int[9];
    int cptHELP = 3;
    Random rnd1 = new Random();
    Random rnd2 = new Random();
    List indicesChoisis = new ArrayList();
    List indicesBTNHELP = new ArrayList();
    int indice1;
    int indice2;
    MediaPlayer mymedia1;
    int nbHELP = 0;
    int score;

    //Les variables qui nous permettront de récupérer les composants
    Button b_submit;
    Button b_newgame;
    Button b_exitgame;
    Button b_help;
    Chronometer mytemp;
    EditText eCELL1;
    EditText eCELL2;
    EditText eCELL3;
    EditText eCELL4;
    EditText eCELL5;
    EditText eCELL6;
    EditText eCELL7;
    EditText eCELL8;
    EditText eCELL9;
    EditText e01;
    TextView resultL1;
    TextView resultL2;
    TextView resultL3;
    TextView resultC1;
    TextView resultC2;
    TextView resultC3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //récupération des composants
        b_submit = (Button) findViewById(R.id.btnSubmit);
        b_newgame = (Button) findViewById(R.id.btnNGAME);
        b_exitgame = (Button) findViewById(R.id.btnEGAME);
        b_help = (Button) findViewById(R.id.btnHELP);
        mytemp = (Chronometer) findViewById(R.id.temp);
        eCELL1 = (EditText) findViewById(R.id.e1);
        eCELL2 = (EditText) findViewById(R.id.e2);
        eCELL3 = (EditText) findViewById(R.id.e3);
        eCELL4 = (EditText) findViewById(R.id.e4);
        eCELL5 = (EditText) findViewById(R.id.e5);
        eCELL6 = (EditText) findViewById(R.id.e6);
        eCELL7 = (EditText) findViewById(R.id.e7);
        eCELL8 = (EditText) findViewById(R.id.e8);
        eCELL9 = (EditText) findViewById(R.id.e9);
        e01 = (EditText) findViewById(R.id.texte);
        resultL1 = (TextView) findViewById(R.id.LINE1);
        resultL2 = (TextView) findViewById(R.id.LINE2);
        resultL3 = (TextView) findViewById(R.id.LINE3);
        resultC1 = (TextView) findViewById(R.id.COLUMN1);
        resultC2 = (TextView) findViewById(R.id.COLUMN2);
        resultC3 = (TextView) findViewById(R.id.COLUMN3);

        mytemp.start();

        //Les positions de notre Game
        for (int i = 0; i < 9; i++) {
            int valeurIndex = genIndiceOnce();
            int valeur = numbersAllowed[valeurIndex];
            tabGAME[i] = valeur;
        }
        sommeL1 = tabGAME[0] + tabGAME[1] + tabGAME[2];
        sommeL2 = tabGAME[3] + tabGAME[4] + tabGAME[5];
        sommeL3 = tabGAME[6] + tabGAME[7] + tabGAME[8];
        sommeC1 = tabGAME[0] + tabGAME[3] + tabGAME[6];
        sommeC2 = tabGAME[1] + tabGAME[4] + tabGAME[7];
        sommeC3 = tabGAME[2] + tabGAME[5] + tabGAME[8];
        resultL3.setText("" + sommeL1);
        resultL2.setText("" + sommeL2);
        resultL3.setText("" + sommeL3);
        resultC1.setText("" + sommeC1);
        resultC2.setText("" + sommeC2);
        resultC3.setText("" + sommeC3);


        //Rendre la consigne non clickable
        e01.setEnabled(false);

        b_submit.setOnClickListener(new Soumettre());
        b_newgame.setOnClickListener(new NouveauJeu());
        b_exitgame.setOnClickListener(new ExitGame());
        b_help.setOnClickListener(new NeedHelp());
    }

    private int genIndiceOnce() {
        int val = rnd1.nextInt(9);
        if (indicesChoisis.contains(val)) {
            genIndiceOnce();
        } else {
            indice1 = val;
            indicesChoisis.add(val);
        }
        return indice1;
    }


    private class Soumettre implements View.OnClickListener {
        //le bouton Submit
        @Override
        public void onClick(View v) {
            //verification si tous les cases ont bien été remplies
            if (!eCELL1.getText().toString().equals("") && !eCELL2.getText().toString().equals("") &&
                    !eCELL3.getText().toString().equals("") && !eCELL4.getText().toString().equals("") &&
                    !eCELL5.getText().toString().equals("") && !eCELL6.getText().toString().equals("") &&
                    !eCELL7.getText().toString().equals("") && !eCELL8.getText().toString().equals("") &&
                    !eCELL9.getText().toString().equals("")) {
                if (checkNumbers() == 0) {
                    afficherDialbox("Chiffre Zéro", "Le nombre 0 est refusé");
                } else if (checkNumbers() == 1) {
                    afficherDialbox("Valeurs identiques", "Eviter d'avoir deux valeurs identiques");
                } else {
                    if (correctAnswers()) {
                        b_newgame.setEnabled(true);
                        mytemp.stop();
                        afficherDialbox("Bonne Réponse", "Félicitations!!!");
                        b_help.setEnabled(false);

                    } else {
                        afficherDialbox("Mauvaise Réponse", "Vérifier la position des chiffres");
                        b_newgame.setEnabled(true);
                    }
                }
            } else {
                afficherDialbox("Chiffre manquant", "Remplissez toutes les cases");
            }
        }
    }

    private boolean correctAnswers() {
        int nb1 = Integer.parseInt(eCELL1.getText().toString());
        int nb2 = Integer.parseInt(eCELL2.getText().toString());
        int nb3 = Integer.parseInt(eCELL3.getText().toString());
        int nb4 = Integer.parseInt(eCELL4.getText().toString());
        int nb5 = Integer.parseInt(eCELL5.getText().toString());
        int nb6 = Integer.parseInt(eCELL6.getText().toString());
        int nb7 = Integer.parseInt(eCELL7.getText().toString());
        int nb8 = Integer.parseInt(eCELL8.getText().toString());
        int nb9 = Integer.parseInt(eCELL9.getText().toString());
        if (nb1 == tabGAME[0] && nb2 == tabGAME[1] && nb3 == tabGAME[2]
                && nb4 == tabGAME[3] && nb5 == tabGAME[4] && nb6 == tabGAME[5]
                && nb7 == tabGAME[6] && nb8 == tabGAME[7] && nb9 == tabGAME[8]) {
            return true;
        } else {
            return false;
        }

    }

    private void afficherDialbox(String s, String s1) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(s);
        alertDialog.setMessage(s1);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }

    private int checkNumbers() {
        int nb1 = Integer.parseInt(eCELL1.getText().toString());
        int nb2 = Integer.parseInt(eCELL2.getText().toString());
        int nb3 = Integer.parseInt(eCELL3.getText().toString());
        int nb4 = Integer.parseInt(eCELL4.getText().toString());
        int nb5 = Integer.parseInt(eCELL5.getText().toString());
        int nb6 = Integer.parseInt(eCELL6.getText().toString());
        int nb7 = Integer.parseInt(eCELL7.getText().toString());
        int nb8 = Integer.parseInt(eCELL8.getText().toString());
        int nb9 = Integer.parseInt(eCELL9.getText().toString());
        int[] tabValues = {nb1, nb2, nb3, nb4, nb5, nb6, nb7, nb8, nb9};

        for (int i = 0; i < 9; i++) {
            if (tabValues[i] == 0) {
                return 0;
            }
        }
        for (int i = 0; i < (9 - 1); i++) {
            for (int j = i + 1; j < 9; j++) {
                if (tabValues[i] == tabValues[j]) {
                    return 1;
                }
            }
        }
        return 2;

    }

    private class NouveauJeu implements View.OnClickListener {


        @Override
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    private class ExitGame implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            finish();
        }
    }

    private class NeedHelp implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            nbHELP++;
            int index = genIndiceForHelp();
            switch (index) {
                case 0:
                    eCELL1.setText("" + tabGAME[0]);
                    eCELL1.setEnabled(false);
                    eCELL1.setBackgroundColor(getResources().getColor(R.color.green_clair));
                    break;
                case 1:
                    eCELL2.setText("" + tabGAME[1]);
                    eCELL2.setEnabled(false);
                    eCELL2.setBackgroundColor(getResources().getColor(R.color.green_clair));
                    break;
                case 2:
                    eCELL3.setText("" + tabGAME[2]);
                    eCELL3.setEnabled(false);
                    eCELL3.setBackgroundColor(getResources().getColor(R.color.green_clair));
                    break;
                case 3:
                    eCELL4.setText("" + tabGAME[3]);
                    eCELL4.setEnabled(false);
                    eCELL4.setBackgroundColor(getResources().getColor(R.color.green_clair));
                    break;
                case 4:
                    eCELL5.setText("" + tabGAME[4]);
                    eCELL5.setEnabled(false);
                    eCELL5.setBackgroundColor(getResources().getColor(R.color.green_clair));
                    break;
                case 5:
                    eCELL6.setText("" + tabGAME[5]);
                    eCELL6.setEnabled(false);
                    eCELL6.setBackgroundColor(getResources().getColor(R.color.green_clair));
                    break;
                case 6:
                    eCELL7.setText("" + tabGAME[6]);
                    eCELL7.setEnabled(false);
                    eCELL7.setBackgroundColor(getResources().getColor(R.color.green_clair));
                    break;
                case 7:
                    eCELL8.setText("" + tabGAME[7]);
                    eCELL8.setEnabled(false);
                    eCELL8.setBackgroundColor(getResources().getColor(R.color.green_clair));
                    break;
                case 8:
                    eCELL9.setText("" + tabGAME[8]);
                    eCELL9.setEnabled(false);
                    eCELL9.setBackgroundColor(getResources().getColor(R.color.green_clair));
                    break;
                default:
                    Log.i("Erreur", "Valeur non correspondante");
                    break;
            }
            cptHELP--;
            b_help.setText("HELP : " + cptHELP);
            if (cptHELP == 0) {
                b_help.setEnabled(false);
            }
        }
    }

    private int genIndiceForHelp() {
        int val = rnd2.nextInt(9);
        if (indicesBTNHELP.contains(val)) {
            genIndiceForHelp();
        } else {
            indice2 = val;
            indicesBTNHELP.add(val);
        }
        return indice2;
    }
}





