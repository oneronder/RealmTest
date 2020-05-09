package com.oneronder.realmtest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    Realm realm;
    EditText kullanici, sifre, isim;
    RadioGroup cinsiyetGroup;
    Button button, guncelleButton;
    ListView listView;
    Integer positionT = 0;
    String cinsiyetText, KullaniciAdiText, isimText, sifreText;
    RealmResults<KisiBilgileri> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realmTanimla();
        tanimla();
        degerAl();
        goster();
        pozisyonBul();

    }

    public void realmTanimla() {
        realm = Realm.getDefaultInstance();
    }

    public void tanimla() {
        kullanici = findViewById(R.id.editTextKullanici);
        sifre = findViewById(R.id.editTextSifre);
        isim = findViewById(R.id.editTextIsim);
        cinsiyetGroup = findViewById(R.id.cinsiyetRadio);
        button = findViewById(R.id.kayitOlButon);
        guncelleButton = findViewById(R.id.guncelleButon);
        listView = findViewById(R.id.listView);
    }
    public void degerAl() {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bilgileriAl();

                yaz(cinsiyetText, isimText, KullaniciAdiText, sifreText);
                kullanici.setText("");
                sifre.setText("");
                isim.setText("");
                ((RadioButton) cinsiyetGroup.getChildAt(0)).setChecked(false);
                ((RadioButton) cinsiyetGroup.getChildAt(1)).setChecked(false);
                goster();
            }
        });

        guncelleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                veriTabanindanListeGetir();

                final KisiBilgileri kisi = list.get(positionT);

                bilgileriAl();

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        kisi.setCinsiyet(cinsiyetText);
                        kisi.setKullaniciAdi(KullaniciAdiText);
                        kisi.setSifre(sifreText);
                        kisi.setIsim(isimText);
                    }
                });
                Toast.makeText(getApplicationContext(), "Güncelleme İşlemi Başarılı", Toast.LENGTH_LONG).show();
                goster();
                kullanici.setText("");
                sifre.setText("");
                isim.setText("");
                ((RadioButton) cinsiyetGroup.getChildAt(0)).setChecked(false);
                ((RadioButton) cinsiyetGroup.getChildAt(1)).setChecked(false);

            }
        });
    }
    public void yaz(final String cinsiyet, final String isim, final String kullanici, final String sifre) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                KisiBilgileri kisiBilgileri = realm.createObject(KisiBilgileri.class);
                kisiBilgileri.setCinsiyet(cinsiyet);
                kisiBilgileri.setSifre(sifre);
                kisiBilgileri.setKullaniciAdi(kullanici);
                kisiBilgileri.setIsim(isim);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {

                Toast.makeText(getApplicationContext(), "Kayıt İşlemi Başarılı", Toast.LENGTH_LONG).show();
                goster();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {

                Toast.makeText(getApplicationContext(), " Kayıt İşlemi Başarılı Değil", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void goster() {

        veriTabanindanListeGetir();
        if (list.size() > 0) {
            adapter adapter = new adapter(list, getApplicationContext());
            listView.setAdapter(adapter);
        }
    }
    public void pozisyonBul() {

        veriTabanindanListeGetir();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                alertAc(position);
                kullanici.setText(list.get(position).getKullaniciAdi());
                sifre.setText(list.get(position).getSifre());
                isim.setText(list.get(position).getIsim());
                if (list.get(position).getCinsiyet().equals("Erkek")) {

                    ((RadioButton) cinsiyetGroup.getChildAt(0)).setChecked(true);

                } else {

                    ((RadioButton) cinsiyetGroup.getChildAt(1)).setChecked(true);

                }
                positionT = position;
            }
        });

    }
    public void sil(final int position) {

        veriTabanindanListeGetir();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                KisiBilgileri kisi = list.get(position);
                kisi.deleteFromRealm();
                goster();
                kullanici.setText("");
                sifre.setText("");
                isim.setText("");
            }
        });
    }
    public void alertAc(final int position) {

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alertlayaut, null);
        Button evetButton = view.findViewById(R.id.evetButton);
        Button hayirButton = view.findViewById(R.id.hayirButton);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(view);
        alert.setCancelable(false);

        final AlertDialog dialog = alert.create();
        evetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sil(position);
                dialog.cancel();
            }
        });
        hayirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
    public void bilgileriAl() {

        Integer id = cinsiyetGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(id);
        cinsiyetText = radioButton.getText().toString();
        isimText = isim.getText().toString();
        KullaniciAdiText = kullanici.getText().toString();
        sifreText = sifre.getText().toString();
    }
    public void veriTabanindanListeGetir() {
        list = realm.where(KisiBilgileri.class).findAll();
    }

}

