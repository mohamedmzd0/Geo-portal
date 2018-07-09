package com.example.mohamedabdelaziz.geo.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.mohamedabdelaziz.geo.ClinicsResponse;
import com.example.mohamedabdelaziz.geo.CompanyOrdersResponse;
import com.example.mohamedabdelaziz.geo.CompanyProductsResponse;
import com.example.mohamedabdelaziz.geo.Constants;
import com.example.mohamedabdelaziz.geo.FeedBackResponse;
import com.example.mohamedabdelaziz.geo.GovernatesResponse;
import com.example.mohamedabdelaziz.geo.HospitalsResponse;
import com.example.mohamedabdelaziz.geo.MedicalReps;
import com.example.mohamedabdelaziz.geo.MedicalRepsResponse;
import com.example.mohamedabdelaziz.geo.MyOrdersResponse;
import com.example.mohamedabdelaziz.geo.OrdersResponse;
import com.example.mohamedabdelaziz.geo.PharmacyResponse;
import com.example.mohamedabdelaziz.geo.Product;
import com.example.mohamedabdelaziz.geo.SalesResponse;
import com.example.mohamedabdelaziz.geo.TasksResponse;
import com.example.mohamedabdelaziz.geo.UsersResponse;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {


    public DbHelper(Context context) {
        super(context, ContractClass.DATABASE_NAME, null, 6);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + ContractClass.TABLE_ECOMMERCE_PRODUCTS + " ( " + ContractClass.PRODUCT_ID + " INTEGER PRIMARY KEY , " + ContractClass.MEDICINE_NAME + " TEXT ," +
                ContractClass.PRICE + " TEXT ,  " + ContractClass.DESCRIPTION + " TEXT , " + ContractClass.PRECAUTIONS + " TEXT , " + ContractClass.IMAGE + " , TEXT  )");
        /////////////////////////////////////
        sqLiteDatabase.execSQL("CREATE TABLE " + ContractClass.TABLE_ECOMMERCE_ORDERS + " ( " + ContractClass.PRODUCT_ID + " INTEGER PRIMARY KEY , " + ContractClass.DATE +
                " TEXT , " + ContractClass.Quantity + " TEXT , " + ContractClass.MEDICINE_NAME + " TEXT , " + ContractClass.PRICE + " TEXT )");
        //////////////////////
        sqLiteDatabase.execSQL("CREATE TABLE " + ContractClass.TABLE_TASKS_MEDICAL + "( " + ContractClass.PRODUCT_ID + " INTEGER PRIMARY KEY ," +
                ContractClass.MEDICINE_NAME + " TEXT , " + ContractClass.PRECAUTIONS + " TEXT , " + ContractClass.DESCRIPTION + " TEXT , " + ContractClass.PRICE + "  TEXT , " + Constants.LAT + " TEXT , " + Constants.LONG + " TEXT )");
        /////////////////
        sqLiteDatabase.execSQL("CREATE TABLE " + ContractClass.TABLE_ORDERS_MEDICAL + " ( INTEGER PRIMARY KEY, " + ContractClass.PRODUCT_ID + " TEXT , " + ContractClass.DATE + " TEXT ," +
                ContractClass.Quantity + " TEXT , " + ContractClass.MEDICINE_NAME + " TEXT , " + ContractClass.USER_NAME + " TEXT , " + ContractClass.PRICE + " TEXT ) ");
        /////////////////////////////////
        sqLiteDatabase.execSQL("CREATE TABLE " + ContractClass.TABLE_GOVERNATES + " ( " + ContractClass.GOVERNATE_ID + " INTEGER PRIMARY KEY , " + ContractClass.GOVERNATE_NAME + " TEXT )");
        ///////////////
        sqLiteDatabase.execSQL("CREATE TABLE " + ContractClass.TABLE_COUNTRY + " ( " + ContractClass.GOVERNATE_ID + " INTEGER PRIMARY KEY , " + ContractClass.GOVERNATE_NAME + " TEXT )");
        /////////////////
        sqLiteDatabase.execSQL("CREATE TABLE " + ContractClass.TABLE_HOSPITALS + " ( " + ContractClass.HOSPITAL_ID + " INTEGET PRIMARY KEY , " + ContractClass.HOSPITAL_NAME + " TEXT ," +
                ContractClass.GOVERNATE_ID + " TEXT , " + ContractClass.LAT + " TEXT , " + ContractClass.LONG + " TEXT  ) ");
        /////////////////////
        sqLiteDatabase.execSQL("CREATE TABLE " + ContractClass.TABLE_CLINICS + " ( " + ContractClass.CLINIC_ID + " INTEGER PRIMARY KEY , " + ContractClass.CLINIC_NAME + " TEXT , " +
                ContractClass.GOVERNATE_ID + " TEXT , " + ContractClass.LAT + " TEXT, " + ContractClass.LONG + " TEXT , " + ContractClass.OPEN + " TEXT , " +
                ContractClass.CLOSE + " TEXT   ) ");
        ///////
        sqLiteDatabase.execSQL("CREATE TABLE " + ContractClass.TABLE_PHARMACY + " ( " + ContractClass.CLINIC_ID + " INTEGER PRIMARY KEY , " + ContractClass.CLINIC_NAME + " TEXT , " +
                ContractClass.GOVERNATE_ID + " TEXT , " + ContractClass.LAT + " TEXT, " + ContractClass.LONG + " TEXT , " + ContractClass.OPEN + " TEXT , " +
                ContractClass.CLOSE + " TEXT   ) ");
        ////////////////////////////////
        sqLiteDatabase.execSQL("CREATE TABLE " + ContractClass.TABLE_MEDICALREPS + " ( " + ContractClass.MEDICAL_ID + " INTEGER PRIMARY KEY ," +
                ContractClass.NAME + " TEXT , " + ContractClass.EMAIL + " TEXT , " + ContractClass.LOCATION + " TEXT , " + ContractClass.TELEPHONE + " TEXT ) ");
        ////////////////////
        sqLiteDatabase.execSQL("CREATE TABLE " + ContractClass.TABLE_USERS + " ( " + ContractClass.USER_ID + " INTEGER PRIMARY KEY , " + ContractClass.USER_NAME + " TEXT , " +
                ContractClass.EMAIL + " TEXT, " + ContractClass.LAT + " TEXT, " + ContractClass.LONG + " TEXT , " + ContractClass.TELEPHONE + " TEXT  )");
        ////////////////
        sqLiteDatabase.execSQL("CREATE TABLE " + ContractClass.TABLE_SALES + " ( " + ContractClass.SALE_ID + " INTEGER PRIMARY KEY , " + ContractClass.SALES + " TEXT , " + ContractClass.TITLE + " TEXT ) ");
        //////////////////////
        sqLiteDatabase.execSQL("CREATE TABLE " + ContractClass.TABLE_FEEDBACK + " ( " + ContractClass.FEEDBACK_id + " INTEGER PRIMARY KEY , " + ContractClass.CONTENT + " TEXT , " +
                ContractClass.DATE + " TEXT ," + ContractClass.MEDICINE_NAME + " TEXT , " + ContractClass.USER_NAME + " TEXT  )");
        //////////////////////
        sqLiteDatabase.execSQL("CREATE TABLE " + ContractClass.TABLE_COMPANY_ORDERS + " ( " + ContractClass.PRODUCT_ID + " INTEGER PRIMARY KEY , " + ContractClass.DATE +
                " TEXT , " + ContractClass.Quantity + " TEXT , " + ContractClass.MEDICINE_NAME + " TEXT , " + ContractClass.PRICE + " TEXT , " + ContractClass.USER_NAME + " TEXT )");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ContractClass.TABLE_ECOMMERCE_PRODUCTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ContractClass.TABLE_ECOMMERCE_ORDERS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ContractClass.TABLE_TASKS_MEDICAL);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ContractClass.TABLE_ORDERS_MEDICAL);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ContractClass.TABLE_GOVERNATES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ContractClass.TABLE_COUNTRY);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ContractClass.TABLE_HOSPITALS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ContractClass.TABLE_CLINICS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ContractClass.TABLE_PHARMACY);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ContractClass.TABLE_MEDICALREPS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ContractClass.TABLE_USERS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ContractClass.TABLE_SALES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ContractClass.TABLE_FEEDBACK);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ContractClass.TABLE_COMPANY_ORDERS);
        onCreate(sqLiteDatabase);
    }

    public void addEcommerceProducts(List<Product> products) {
        SQLiteDatabase sqld = this.getWritableDatabase();
        sqld.delete(ContractClass.TABLE_ECOMMERCE_PRODUCTS, null, null);
        for (int i = 0; i < products.size(); i++) {
            ContentValues cv = new ContentValues();
            cv.put(ContractClass.PRODUCT_ID, products.get(i).getId());
            cv.put(ContractClass.MEDICINE_NAME, products.get(i).getMedcineName());
            cv.put(ContractClass.PRICE, products.get(i).getPrice());
            cv.put(ContractClass.DESCRIPTION, products.get(i).getDescription());
            cv.put(ContractClass.PRECAUTIONS, products.get(i).getPrecautions());
            try {
                Log.d("DBhelper", "addEcommerceProducts" + sqld.insert(ContractClass.TABLE_ECOMMERCE_PRODUCTS, null, cv));
            } catch (SQLiteConstraintException exception) {
            }
        }
    }

    public List<Product> getEcommerceProducts() {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + ContractClass.TABLE_ECOMMERCE_PRODUCTS, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            Product product = new Product();
            product.setId(String.valueOf(cursor.getInt(cursor.getColumnIndex(ContractClass.PRODUCT_ID))));
            product.setDescription(cursor.getString(cursor.getColumnIndex(ContractClass.DESCRIPTION)));
            product.setImage(cursor.getString(cursor.getColumnIndex(ContractClass.IMAGE)));
            product.setMedcineName(cursor.getString(cursor.getColumnIndex(ContractClass.MEDICINE_NAME)));
            product.setPrecautions(cursor.getString(cursor.getColumnIndex(ContractClass.PRECAUTIONS)));
            product.setPrice(cursor.getString(cursor.getColumnIndex(ContractClass.PRICE)));
            products.add(product);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return products;
    }

    public void addEcommerceOrders(ArrayList<MyOrdersResponse.MyProduct> body) {
        SQLiteDatabase sqld = this.getWritableDatabase();
        sqld.delete(ContractClass.TABLE_ECOMMERCE_ORDERS, null, null);
        for (int i = 0; i < body.size(); i++) {
            ContentValues cv = new ContentValues();
            cv.put(ContractClass.PRODUCT_ID, body.get(i).getId());
            cv.put(ContractClass.MEDICINE_NAME, body.get(i).getMedcineName());
            cv.put(ContractClass.PRICE, body.get(i).getPrice());
            cv.put(ContractClass.Quantity, body.get(i).getQuantity());
            cv.put(ContractClass.DATE, body.get(i).getDate());
            try {
                Log.d("DBhelper", "addEcommerceOrders" + sqld.insert(ContractClass.TABLE_ECOMMERCE_ORDERS, null, cv));
            } catch (SQLiteConstraintException exception) {
            }
        }
    }

    public ArrayList<MyOrdersResponse.MyProduct> getEcommerceOrders() {
        ArrayList<MyOrdersResponse.MyProduct> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + ContractClass.TABLE_ECOMMERCE_ORDERS, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            MyOrdersResponse.MyProduct product = new MyOrdersResponse.MyProduct();
            product.setId(cursor.getString(cursor.getColumnIndex(ContractClass.PRODUCT_ID)));
            product.setDate(cursor.getString(cursor.getColumnIndex(ContractClass.DATE)));
            product.setQuantity(cursor.getString(cursor.getColumnIndex(ContractClass.Quantity)));
            product.setMedcineName(cursor.getString(cursor.getColumnIndex(ContractClass.MEDICINE_NAME)));
            product.setPrice(cursor.getString(cursor.getColumnIndex(ContractClass.PRICE)));
            products.add(product);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return products;
    }

    public void addMedicalTasks(List<TasksResponse.Task> tasks) {
        SQLiteDatabase sqld = this.getWritableDatabase();
        sqld.delete(ContractClass.TABLE_TASKS_MEDICAL, null, null);
        for (int i = 0; i < tasks.size(); i++) {
            ContentValues cv = new ContentValues();
            cv.put(ContractClass.PRODUCT_ID, tasks.get(i).getId());
            cv.put(ContractClass.MEDICINE_NAME, tasks.get(i).getName());
            cv.put(ContractClass.PRICE, tasks.get(i).getPrice());
            cv.put(ContractClass.DESCRIPTION, tasks.get(i).getDescription());
            cv.put(ContractClass.PRECAUTIONS, tasks.get(i).getPrecautions());
            cv.put(ContractClass.LAT, tasks.get(i).getLat());
            cv.put(ContractClass.LONG, tasks.get(i).getLng());
            try {
                Log.d("DBhelper", "addMedicalTasks" + sqld.insert(ContractClass.TABLE_TASKS_MEDICAL, null, cv));
            } catch (SQLiteConstraintException exception) {
            }
        }
    }

    public List<TasksResponse.Task> getMedicalTasks() {
        List<TasksResponse.Task> values = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + ContractClass.TABLE_TASKS_MEDICAL, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            TasksResponse.Task value = new TasksResponse.Task();
            value.setId(cursor.getString(cursor.getColumnIndex(ContractClass.PRODUCT_ID)));
            value.setDescription(cursor.getString(cursor.getColumnIndex(ContractClass.DESCRIPTION)));
            value.setPrecautions(cursor.getString(cursor.getColumnIndex(ContractClass.PRECAUTIONS)));
            value.setName(cursor.getString(cursor.getColumnIndex(ContractClass.MEDICINE_NAME)));
            value.setPrice(cursor.getString(cursor.getColumnIndex(ContractClass.PRICE)));
            value.setLat(cursor.getString(cursor.getColumnIndex(ContractClass.LAT)));
            value.setLng(cursor.getString(cursor.getColumnIndex(ContractClass.LONG)));
            values.add(value);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return values;
    }

    public void addMEdicalOrders(List<OrdersResponse.OrderProduct> products) {
        SQLiteDatabase sqld = this.getWritableDatabase();
        sqld.delete(ContractClass.TABLE_ORDERS_MEDICAL, null, null);
        if (products == null)
            return;
        for (int i = 0; i < products.size(); i++) {
            ContentValues cv = new ContentValues();
            cv.put(ContractClass.PRODUCT_ID, products.get(i).getId());
            cv.put(ContractClass.DATE, products.get(i).getDate());
            cv.put(ContractClass.PRICE, products.get(i).getPrice());
            cv.put(ContractClass.MEDICINE_NAME, products.get(i).getMedcineName());
            cv.put(ContractClass.Quantity, products.get(i).getQuantity());
            cv.put(ContractClass.USER_NAME, products.get(i).getUserName());
            try {
                Log.d("DBhelper", "addMedicalTasks" + sqld.insert(ContractClass.TABLE_ORDERS_MEDICAL, null, cv));
            } catch (SQLiteConstraintException exception) {
            }
        }
    }

    public List<OrdersResponse.OrderProduct> geMedicalOrders() {
        List<OrdersResponse.OrderProduct> values = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + ContractClass.TABLE_ORDERS_MEDICAL, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            OrdersResponse.OrderProduct value = new OrdersResponse.OrderProduct();
            value.setId(cursor.getString(cursor.getColumnIndex(ContractClass.PRODUCT_ID)));
            value.setDate(cursor.getString(cursor.getColumnIndex(ContractClass.DATE)));
            value.setMedcineName(cursor.getString(cursor.getColumnIndex(ContractClass.MEDICINE_NAME)));
            value.setUserName(cursor.getString(cursor.getColumnIndex(ContractClass.USER_NAME)));
            value.setPrice(cursor.getString(cursor.getColumnIndex(ContractClass.PRICE)));
            value.setQuantity(cursor.getString(cursor.getColumnIndex(ContractClass.Quantity)));
            values.add(value);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return values;
    }

    public void addGovernates(List<GovernatesResponse.Governate> governates) {
        SQLiteDatabase sqld = this.getWritableDatabase();
        sqld.delete(ContractClass.TABLE_GOVERNATES, null, null);
        for (int i = 0; i < governates.size(); i++) {
            ContentValues cv = new ContentValues();
            cv.put(ContractClass.GOVERNATE_ID, governates.get(i).getId());
            cv.put(ContractClass.GOVERNATE_NAME, governates.get(i).getName());
            try {
                Log.d("DBhelper", "addGovernates" + sqld.insert(ContractClass.TABLE_GOVERNATES, null, cv));
            } catch (SQLiteConstraintException exception) {
                Log.d("DBhelper", "addGovernates" + exception.getLocalizedMessage());
            }
        }
    }

    public void addCountries(List<GovernatesResponse.Governate> governates) {
        SQLiteDatabase sqld = this.getWritableDatabase();
        sqld.delete(ContractClass.TABLE_COUNTRY, null, null);
        for (int i = 0; i < governates.size(); i++) {
            ContentValues cv = new ContentValues();
            cv.put(ContractClass.GOVERNATE_ID, governates.get(i).getId());
            cv.put(ContractClass.GOVERNATE_NAME, governates.get(i).getName());
            try {
                Log.d("DBhelper", "addcounties" + sqld.insert(ContractClass.TABLE_COUNTRY, null, cv));
            } catch (SQLiteConstraintException exception) {
                Log.d("DBhelper", "addcounties" + exception.getLocalizedMessage());
            }
        }
    }

    public List<GovernatesResponse.Governate> getGovernates() {
        List<GovernatesResponse.Governate> values = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + ContractClass.TABLE_GOVERNATES, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            GovernatesResponse.Governate value = new GovernatesResponse.Governate(cursor.getString(cursor.getColumnIndex(ContractClass.GOVERNATE_ID)), cursor.getString(cursor.getColumnIndex(ContractClass.GOVERNATE_NAME)));
            values.add(value);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return values;
    }

    public List<GovernatesResponse.Governate> getCountries() {
        List<GovernatesResponse.Governate> values = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + ContractClass.TABLE_COUNTRY, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            GovernatesResponse.Governate value = new GovernatesResponse.Governate(cursor.getString(cursor.getColumnIndex(ContractClass.GOVERNATE_ID)), cursor.getString(cursor.getColumnIndex(ContractClass.GOVERNATE_NAME)));
            values.add(value);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return values;
    }

    public void addHospitals(List<HospitalsResponse.Hospital> hospitals) {
        SQLiteDatabase sqld = this.getWritableDatabase();
        sqld.delete(ContractClass.TABLE_HOSPITALS, null, null);
        for (int i = 0; i < hospitals.size(); i++) {
            ContentValues cv = new ContentValues();
            cv.put(ContractClass.HOSPITAL_ID, hospitals.get(i).getId());
            cv.put(ContractClass.HOSPITAL_NAME, hospitals.get(i).getName());
            cv.put(ContractClass.GOVERNATE_ID, hospitals.get(i).getCountryId());
            cv.put(ContractClass.LONG, hospitals.get(i).getLang());
            cv.put(ContractClass.LAT, hospitals.get(i).getLat());
            try {
                Log.d("DBhelper", "addHospitals" + sqld.insert(ContractClass.TABLE_HOSPITALS, null, cv));
            } catch (SQLiteConstraintException exception) {
            }
        }
    }

    public List<HospitalsResponse.Hospital> getHospitals() {
        List<HospitalsResponse.Hospital> hospitals = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + ContractClass.TABLE_HOSPITALS, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            HospitalsResponse.Hospital value = new HospitalsResponse.Hospital();
            value.setId(cursor.getString(cursor.getColumnIndex(ContractClass.HOSPITAL_ID)));
            value.setName(cursor.getString(cursor.getColumnIndex(ContractClass.HOSPITAL_NAME)));
            value.setCountryId(cursor.getString(cursor.getColumnIndex(ContractClass.GOVERNATE_ID)));
            value.setLat(cursor.getString(cursor.getColumnIndex(ContractClass.LAT)));
            value.setLang(cursor.getString(cursor.getColumnIndex(ContractClass.LONG)));
            hospitals.add(value);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return hospitals;
    }

    public void addClinics(List<ClinicsResponse.ClinicsProduct> products) {
        SQLiteDatabase sqld = this.getWritableDatabase();
        sqld.delete(ContractClass.TABLE_CLINICS, null, null);
        for (int i = 0; i < products.size(); i++) {
            ContentValues cv = new ContentValues();
            cv.put(ContractClass.CLINIC_ID, products.get(i).getId());
            cv.put(ContractClass.CLINIC_NAME, products.get(i).getName());
            cv.put(ContractClass.CLOSE, products.get(i).getClose());
            cv.put(ContractClass.OPEN, products.get(i).getOpen());
            cv.put(ContractClass.LAT, products.get(i).getLat());
            cv.put(ContractClass.LONG, products.get(i).getLang());
            cv.put(ContractClass.GOVERNATE_ID, products.get(i).getCountryId());
            try {
                Log.d("DBhelper " + products.get(i).getName(), "addClinics" + sqld.insert(ContractClass.TABLE_CLINICS, null, cv));
            } catch (SQLiteConstraintException exception) {
                Log.d("DBhelper", "addClinics" + exception.getLocalizedMessage());
            }
        }
    }

    public List<ClinicsResponse.ClinicsProduct> getClinics() {
        List<ClinicsResponse.ClinicsProduct> clinics = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + ContractClass.TABLE_CLINICS, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            ClinicsResponse.ClinicsProduct clinicsProduct = new ClinicsResponse.ClinicsProduct();
            clinicsProduct.setId(String.valueOf(cursor.getInt(cursor.getColumnIndex(ContractClass.CLINIC_ID))));
            clinicsProduct.setName(String.valueOf(cursor.getInt(cursor.getColumnIndex(ContractClass.CLINIC_NAME))));
            clinicsProduct.setOpen(String.valueOf(cursor.getInt(cursor.getColumnIndex(ContractClass.OPEN))));
            clinicsProduct.setClose(String.valueOf(cursor.getInt(cursor.getColumnIndex(ContractClass.CLOSE))));
            clinicsProduct.setLat(String.valueOf(cursor.getInt(cursor.getColumnIndex(ContractClass.LAT))));
            clinicsProduct.setLang(String.valueOf(cursor.getInt(cursor.getColumnIndex(ContractClass.LONG))));
            clinicsProduct.setCountryId(String.valueOf(cursor.getInt(cursor.getColumnIndex(ContractClass.GOVERNATE_ID))));
            clinics.add(clinicsProduct);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();

        return clinics;
    }

    public void addPharmacies(List<PharmacyResponse.PharmacyProduct> pharmacyProducts) {
        SQLiteDatabase sqld = this.getWritableDatabase();
        sqld.delete(ContractClass.TABLE_PHARMACY, null, null);
        for (int i = 0; i < pharmacyProducts.size(); i++) {
            ContentValues cv = new ContentValues();
            cv.put(ContractClass.CLINIC_ID, pharmacyProducts.get(i).getId());
            cv.put(ContractClass.CLINIC_NAME, pharmacyProducts.get(i).getName());
            cv.put(ContractClass.CLOSE, pharmacyProducts.get(i).getClose());
            cv.put(ContractClass.OPEN, pharmacyProducts.get(i).getOpen());
            cv.put(ContractClass.LAT, pharmacyProducts.get(i).getLat());
            cv.put(ContractClass.LONG, pharmacyProducts.get(i).getLang());
            cv.put(ContractClass.GOVERNATE_ID, pharmacyProducts.get(i).getCountryId());
            try {
                Log.d("DBhelper " + pharmacyProducts.get(i).getName(), "addPharmacies" + sqld.insert(ContractClass.TABLE_PHARMACY, null, cv));
            } catch (SQLiteConstraintException exception) {
                Log.d("DBhelper", "addPharmacies" + exception.getLocalizedMessage());
            }
        }

    }

    public List<PharmacyResponse.PharmacyProduct> getPharmacies() {
        List<PharmacyResponse.PharmacyProduct> pharamacies = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + ContractClass.TABLE_HOSPITALS, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            PharmacyResponse.PharmacyProduct value = new PharmacyResponse.PharmacyProduct();
            value.setId(cursor.getString(cursor.getColumnIndex(ContractClass.HOSPITAL_ID)));
            value.setName(cursor.getString(cursor.getColumnIndex(ContractClass.HOSPITAL_NAME)));
            value.setCountryId(cursor.getString(cursor.getColumnIndex(ContractClass.GOVERNATE_ID)));
            value.setLat(cursor.getString(cursor.getColumnIndex(ContractClass.LAT)));
            value.setLang(cursor.getString(cursor.getColumnIndex(ContractClass.LONG)));
            pharamacies.add(value);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return pharamacies;
    }

    public void addMember(List<MedicalRepsResponse.MedRep> medReps) {
        SQLiteDatabase sqld = this.getWritableDatabase();
        sqld.delete(ContractClass.TABLE_MEDICALREPS, null, null);
        for (int i = 0; i < medReps.size(); i++) {
            ContentValues cv = new ContentValues();
            cv.put(ContractClass.MEDICAL_ID, medReps.get(i).getId());
            cv.put(ContractClass.NAME, medReps.get(i).getName());
            cv.put(ContractClass.EMAIL, medReps.get(i).getEmail());
            cv.put(ContractClass.LOCATION, medReps.get(i).getLocation());
            cv.put(ContractClass.TELEPHONE, medReps.get(i).getTelphone());

            try {
                Log.d("DBhelper " + medReps.get(i).getName(), "addMember" + sqld.insert(ContractClass.TABLE_MEDICALREPS, null, cv));
            } catch (SQLiteConstraintException exception) {
                Log.d("DBhelper", "addMember" + exception.getLocalizedMessage());
            }
        }
    }

    public List<MedicalRepsResponse.MedRep> getMembers() {
        List<MedicalRepsResponse.MedRep> medReps = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + ContractClass.TABLE_MEDICALREPS, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            MedicalRepsResponse.MedRep value = new MedicalRepsResponse.MedRep();
            value.setId(cursor.getString(cursor.getColumnIndex(ContractClass.MEDICAL_ID)));
            value.setName(cursor.getString(cursor.getColumnIndex(ContractClass.NAME)));
            value.setEmail(cursor.getString(cursor.getColumnIndex(ContractClass.EMAIL)));
            value.setTelphone(cursor.getString(cursor.getColumnIndex(ContractClass.TELEPHONE)));
            value.setLocation(cursor.getString(cursor.getColumnIndex(ContractClass.LOCATION)));
            medReps.add(value);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return medReps;
    }

    public void addUsers(List<UsersResponse.User> users) {
        SQLiteDatabase sqld = this.getWritableDatabase();
        sqld.delete(ContractClass.TABLE_USERS, null, null);
        for (int i = 0; i < users.size(); i++) {
            ContentValues cv = new ContentValues();
            cv.put(ContractClass.USER_ID, users.get(i).getId());
            cv.put(ContractClass.USER_NAME, users.get(i).getName());
            cv.put(ContractClass.EMAIL, users.get(i).getEmail());
            cv.put(ContractClass.LAT, users.get(i).getLat());
            cv.put(ContractClass.LONG, users.get(i).getLng());
            cv.put(ContractClass.TELEPHONE, users.get(i).getTelphone());

            try {
                Log.d("DBhelper " + users.get(i).getName(), "addUsers" + sqld.insert(ContractClass.TABLE_USERS, null, cv));
            } catch (SQLiteConstraintException exception) {
                Log.d("DBhelper", "addUsers" + exception.getLocalizedMessage());
            }
        }
    }

    public List<UsersResponse.User> getUsers() {
        List<UsersResponse.User> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + ContractClass.TABLE_USERS, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            UsersResponse.User value = new UsersResponse.User();
            value.setId(cursor.getString(cursor.getColumnIndex(ContractClass.USER_ID)));
            value.setName(cursor.getString(cursor.getColumnIndex(ContractClass.USER_NAME)));
            value.setEmail(cursor.getString(cursor.getColumnIndex(ContractClass.EMAIL)));
            value.setTelphone(cursor.getString(cursor.getColumnIndex(ContractClass.TELEPHONE)));
            value.setLat(cursor.getString(cursor.getColumnIndex(ContractClass.LAT)));
            value.setLng(cursor.getString(cursor.getColumnIndex(ContractClass.LONG)));
            users.add(value);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();

        return users;
    }

    public void addSales(List<SalesResponse.SalesProduct> salesProducts) {
        SQLiteDatabase sqld = this.getWritableDatabase();
        sqld.delete(ContractClass.TABLE_SALES, null, null);
        for (int i = 0; i < salesProducts.size(); i++) {
            ContentValues cv = new ContentValues();
            cv.put(ContractClass.SALE_ID, salesProducts.get(i).getId());
            cv.put(ContractClass.SALES, salesProducts.get(i).getSales());
            cv.put(ContractClass.TITLE, salesProducts.get(i).getTitle());
            try {
                Log.d("DBhelper " + salesProducts.get(i).getTitle(), "addSales" + sqld.insert(ContractClass.TABLE_SALES, null, cv));
            } catch (SQLiteConstraintException exception) {
                Log.d("DBhelper", "addSales" + exception.getLocalizedMessage());
            }
        }
    }

    public List<SalesResponse.SalesProduct> getSales() {
        List<SalesResponse.SalesProduct> salesProducts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + ContractClass.TABLE_SALES, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            SalesResponse.SalesProduct value = new SalesResponse.SalesProduct();
            value.setId(cursor.getString(cursor.getColumnIndex(ContractClass.SALE_ID)));
            value.setSales(cursor.getString(cursor.getColumnIndex(ContractClass.SALES)));
            value.setTitle(cursor.getString(cursor.getColumnIndex(ContractClass.TITLE)));
            salesProducts.add(value);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return salesProducts;

    }

    public void addFeedback(List<FeedBackResponse.Feedback> feedback) {
        SQLiteDatabase sqld = this.getWritableDatabase();
        sqld.delete(ContractClass.TABLE_FEEDBACK, null, null);
        if (feedback == null)
            return;
        for (int i = 0; i < feedback.size(); i++) {
            ContentValues cv = new ContentValues();
            cv.put(ContractClass.FEEDBACK_id, feedback.get(i).getId());
            cv.put(ContractClass.CONTENT, feedback.get(i).getContent());
            cv.put(ContractClass.DATE, feedback.get(i).getDate());
            cv.put(ContractClass.USER_NAME, feedback.get(i).getUserName());
            cv.put(ContractClass.MEDICINE_NAME, feedback.get(i).getMedcineName());
            try {
                Log.d("DBhelper " + feedback.get(i).getMedcineName(), "addFeedback" + sqld.insert(ContractClass.TABLE_FEEDBACK, null, cv));
            } catch (SQLiteConstraintException exception) {
                Log.d("DBhelper", "addFeedback" + exception.getLocalizedMessage());
            }
        }
    }

    public List<FeedBackResponse.Feedback> getFeedback() {
        List<FeedBackResponse.Feedback> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + ContractClass.TABLE_FEEDBACK, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            FeedBackResponse.Feedback value = new FeedBackResponse.Feedback();
            value.setId(cursor.getString(cursor.getColumnIndex(ContractClass.FEEDBACK_id)));
            value.setContent(cursor.getString(cursor.getColumnIndex(ContractClass.CONTENT)));
            value.setDate(cursor.getString(cursor.getColumnIndex(ContractClass.DATE)));
            value.setUserName(cursor.getString(cursor.getColumnIndex(ContractClass.USER_NAME)));
            value.setMedcineName(cursor.getString(cursor.getColumnIndex(ContractClass.MEDICINE_NAME)));
            list.add(value);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return list;
    }

    public void addCompanyOrders(List<CompanyOrdersResponse.CompanyOrdersProduct> products) {
        SQLiteDatabase sqld = this.getWritableDatabase();
        sqld.delete(ContractClass.TABLE_COMPANY_ORDERS, null, null);
        for (int i = 0; i < products.size(); i++) {
            ContentValues cv = new ContentValues();
            cv.put(ContractClass.PRODUCT_ID, products.get(i).getId());
            cv.put(ContractClass.MEDICINE_NAME, products.get(i).getMedcineName());
            cv.put(ContractClass.PRICE, products.get(i).getPrice());
            cv.put(ContractClass.Quantity, products.get(i).getQuantity());
            cv.put(ContractClass.DATE, products.get(i).getDate());
            cv.put(ContractClass.USER_NAME, products.get(i).getUserName());
            try {
                Log.d("DBhelper", "addCompanyOrders" + sqld.insert(ContractClass.TABLE_COMPANY_ORDERS, null, cv));
            } catch (SQLiteConstraintException exception) {
            }
        }
    }

    public List<CompanyOrdersResponse.CompanyOrdersProduct> getCOmpanyOrders() {
        List<CompanyOrdersResponse.CompanyOrdersProduct> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + ContractClass.TABLE_COMPANY_ORDERS, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            CompanyOrdersResponse.CompanyOrdersProduct product = new CompanyOrdersResponse.CompanyOrdersProduct();
            product.setId(cursor.getString(cursor.getColumnIndex(ContractClass.PRODUCT_ID)));
            product.setDate(cursor.getString(cursor.getColumnIndex(ContractClass.DATE)));
            product.setQuantity(cursor.getString(cursor.getColumnIndex(ContractClass.Quantity)));
            product.setMedcineName(cursor.getString(cursor.getColumnIndex(ContractClass.MEDICINE_NAME)));
            product.setPrice(cursor.getString(cursor.getColumnIndex(ContractClass.PRICE)));
            product.setUserName(cursor.getString(cursor.getColumnIndex(ContractClass.USER_NAME)));
            products.add(product);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return products;
    }

}
