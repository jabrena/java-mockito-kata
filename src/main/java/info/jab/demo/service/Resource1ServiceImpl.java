package info.jab.demo.service;

import info.jab.demo.repository.Resource1Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Resource1ServiceImpl implements Resource1Service {

    private final Resource1Repository resource1Repository;

    public Resource1ServiceImpl(Resource1Repository resource1Repository) {
        this.resource1Repository = resource1Repository;
    }

    @Override
    public List<String> getResources() {
        return resource1Repository.findAll();
    }
}
