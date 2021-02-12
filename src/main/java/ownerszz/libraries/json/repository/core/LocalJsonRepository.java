package ownerszz.libraries.json.repository.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public  class LocalJsonRepository<T> implements JsonRepository<T> {
    private String destination;
    private List<T> objects;
    private ObjectMapper objectMapper;
    private Class<T> clazz;
    public LocalJsonRepository(Class<T> clazz,String destination) throws Exception {
        this.clazz = clazz;
        if (!destination.toLowerCase().endsWith(".json")){
            destination += ".json";
        }
        this.destination = destination;
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        readAll();
    }
    private void readAll() throws Exception {
        File file = Paths.get(destination).toFile();
        if (!file.exists()){
            file.createNewFile();
        }else {
            BufferedReader br = new BufferedReader(new FileReader(file));
            objects = objectMapper.readValue(br,new TypeReference<List<T>>(){});
        }

    }
   /* private void clearFile() throws Exception {
        String str = "";
        BufferedWriter writer = new BufferedWriter(new FileWriter(destination));
        writer.write(str);

        writer.close();
    }*/
    @Override
    public T save(T obj) throws Exception{
        objects.add(obj);
        saveAll();
        return obj;
    }

    @Override
    public Future<T> saveAsync(T obj) throws Exception{
        CompletableFuture<T> future = new CompletableFuture<>();
        future.completeAsync(()->{
            try {
               return save(obj);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return future;
    }

    @Override
    public List<T> findAllBy(Predicate<T> filter){
        List<T> toReturn;
        if(filter == null){
            toReturn = new ArrayList<>(objects);
        }else {
            toReturn = objects.stream().filter(filter).collect(Collectors.toList());
        }
        return toReturn;
    }

    @Override
    public Future<List<T>> findAllByAsync(Predicate<T> filter){
        CompletableFuture<List<T>> future = new CompletableFuture<>();
        future.completeAsync(()->{
            try {
                return findAllBy(filter);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return future;
    }
    @Override
    public Optional<T> findBy(Predicate<T> filter){
        if(filter == null){
            throw new IllegalArgumentException("Filter cannot be null");
        }
        List<T> found =  findAllBy(filter);
        if (found.size() == 0){
            return Optional.empty();
        }
        else if (found.size() > 1){
            throw new RuntimeException("Expected 1 result but got: " + found.size());
        }
        return Optional.ofNullable(found.get(0));
    }
    @Override
    public Future<Optional<T>> findByAsync(Predicate<T> filter){
        CompletableFuture<Optional<T>> future = new CompletableFuture<>();
        future.completeAsync(()->{
            try {
                return findBy(filter);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return future;
    }
    @Override
    public boolean remove(T obj) throws Exception {
       if(objects.remove(obj)){
           saveAll();
           return true;
       }
       return false;

    }
    @Override
    public Future<Boolean> removeAsync(T obj){
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        future.completeAsync(()->{
            try {
                return remove(obj);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return future;
    }

    @Override
    public void saveAll() throws Exception {
        FileWriter writer = new FileWriter(destination,false);
        BufferedWriter bw = new BufferedWriter(writer);
        objectMapper.writeValue(bw,objects);
    }

    @Override
    public Future<Void> saveAllAsync(){
        CompletableFuture<Void> future = new CompletableFuture<>();
        future.completeAsync(()->{
            try {
                saveAll();
                return null;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return future;
    }

    @Override
    public boolean removeAll(List<T> objects) throws Exception {
       if(this.objects.removeAll(objects)){
           saveAll();
           return true;
       }
       return false;
    }
    @Override
    public Future<Boolean> removeAllAsync(List<T> objects){
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        future.completeAsync(()->{
            try {
                return removeAll(objects);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return future;
    }
    @Override
    public boolean removeAllBy(Predicate<T> filter) throws Exception {
        return removeAll(findAllBy(filter));
    }
    @Override
    public Future<Boolean> removeAllByAsync(Predicate<T> filter){
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        future.completeAsync(()->{
            try {
                return removeAllBy(filter);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return future;
    }


}
