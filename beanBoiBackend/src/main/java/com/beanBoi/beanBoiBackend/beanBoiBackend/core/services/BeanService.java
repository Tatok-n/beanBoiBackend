package com.beanBoi.beanBoiBackend.beanBoiBackend.core.services;


import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.Bean;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories.BeanRepository;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories.UserRepository;
import com.google.cloud.firestore.DocumentReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BeanService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private BeanRepository beanRepository;


    public List<Bean> getAllBeansForUser(String userId) throws FileNotFoundException {
        if (userRepository.getUserById(userId) != null) {
            return beanRepository.getActiveBeansForUser(userId);
        }
        throw new FileNotFoundException("User does not exist");
    }

    public Bean getBeanById(String id) throws FileNotFoundException {
        if (beanRepository.getBeanById(id) != null) {
            return beanRepository.getBeanById(id);
        }
        throw new FileNotFoundException("Bean does not exist");
    }

    public void updateBean(String id, Map<String,Object> beanMap, String uid) throws FileNotFoundException {
        Bean bean = (Bean) beanRepository.getFromMap(beanMap);
        bean.setUid(uid);
        beanRepository.saveDocumentWithId(id, bean);

    }

    public Bean addBeanToUser(Bean bean, String uid) throws FileNotFoundException {
        if (userRepository.getUserById(uid) != null) {
            DocumentReference beanRef = beanRepository.saveDocument(bean);
            userRepository.updateDocumentListWithField(uid,beanRef,"beansOwned");
            return beanRepository.getBeanById(uid);
        }
        throw new FileNotFoundException("User does not exist");
    }

    public Bean createNewBean(Map<String, Object> map, String uid) throws FileNotFoundException {
        if (userRepository.getUserById(uid) != null) {
            Bean bean = new Bean();
            bean.setUid(uid);
            bean.setName((String) map.get("name"));
            bean.setProcess((String) map.get("process"));
            bean.setOrigin((String) map.get("origin"));
            bean.setTastingNotes((String) map.get("tastingNotes"));
            bean.setRoaster((String) map.get("roaster"));
            bean.setAltitude(Long.parseLong(map.get("altitude").toString()));
            bean.setPrice(Float.parseFloat(map.get("price").toString()));
            bean.setRoastDegree(Long.parseLong(map.get("roastDegree").toString()));
            bean.setTimesPurchased(0);
            bean.setActive(true);
            DocumentReference beanRef = beanRepository.saveDocument(bean);
            userRepository.updateDocumentListWithField(uid,beanRef,"beansOwned");
            return beanRepository.getBeanById(beanRef.getId());
        }
        throw new FileNotFoundException("User does not exist");
    }

    public void deleteBean(String beanId, String UID) throws FileNotFoundException {
        beanRepository.updateDocumentField(beanId, "isActive", false);
        List<DocumentReference> beanRefs = new ArrayList<>(((List<DocumentReference>) userRepository.getDocumentField(UID, "beansOwned")));
        DocumentReference beanToRemove = beanRefs.stream()
                .filter(doc -> doc.getId().equals(beanId))
                .findFirst().orElse(null);
        if (beanToRemove != null) {
            beanRefs.remove(beanToRemove);
            userRepository.updateDocumentField(UID,"beansOwned",beanRefs);
        }

    }
}
