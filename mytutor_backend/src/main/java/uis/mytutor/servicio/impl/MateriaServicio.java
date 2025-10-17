package uis.mytutor.servicio.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uis.mytutor.modelo.Materia;
import uis.mytutor.repositorio.MateriaRepositorio;
import uis.mytutor.servicio.interfaz.IMateriaServicio;

import java.util.List;

@Service
@Transactional
public class MateriaServicio implements IMateriaServicio {

    @Autowired
    private MateriaRepositorio materiaRepositorio;

    @Override
    public List<Materia> getMaterias(){
        return materiaRepositorio.findAll();
    }

    @Override
    public Materia getMateriaById(Long id){
        return materiaRepositorio.findById(id).orElse(null);
    }

    @Override
    public Materia addMateria(Materia materia){
        if(materia.getId() == null){
            return materiaRepositorio.save(materia);
        }
        return null;
    }

    @Override
    public Materia updateMateria(Materia materia){
        Materia obj = getMateriaById(materia.getId());
        if (obj != null){
            obj.setNombre(materia.getNombre());
            obj.setDescripcion(materia.getDescripcion());
            return materiaRepositorio.save(obj);
        }
        return null;
    }

    @Override
    public boolean deleteMateriaById(Long id){
        Materia obj = getMateriaById(id);
        if(obj != null){
            materiaRepositorio.deleteById(id);
            return true;
        }
        return false;
    }
}
