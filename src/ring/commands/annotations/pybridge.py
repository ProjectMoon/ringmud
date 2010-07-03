"""
Bridge module that enables the templating annotations in Python code.
This module uses a Template decorator to simulate the @Template annotation.
The decorator takes specialized classes that have the same name as their
Java counterparts. User code should only need to import this module, not
the actual annotations.

NOTE: This is not an enabling of Java annotations in Python. It is a
simulation of annotations used to solve a domain-specific problem.
""" 
from ring.commands.annotations import BindType as BindTypeAnnotation
from ring.commands.annotations import Form as FormAnnotation
from ring.commands.annotations import Template as TemplateAnnotation
from ring.commands.annotations import Scope

class BindType(BindTypeAnnotation):
    """
    Bridge class that implements the BindType annotation interface.
    """
    def __init__(self, bindTypes):
        if hasattr(bindTypes, "__iter__"):
            self.bindTypes = bindTypes
        else:
            raise ValueError("BindType requires list of data types")
            
    def value(self):
        return self.bindTypes
    
    def __getitem__(self, index):
        return self.bindTypes[index]
    
class Form(FormAnnotation):
    """
    Bridge class that implements the Form annotation interface
    """ 
    def __init__(self, id = "default", clause = "", bind = [], scope = Scope.ROOM):
        self._id = id
        self._clause = clause
        self._bind = bind
        self._scope = scope
        
    def id(self):
        return self._id
    
    def clause(self):
        return self._clause
    
    def bind(self):
        return self._bind
    
    def scope(self):
        return self._scope

class TemplateBridge(TemplateAnnotation):
    """
    Bridge class that implements the Template annotation interface.
    """
    def __init__(self, forms):
        self.forms = forms
    
    def value(self):
        return self.forms
    
    def __getitem__(self, index):
        return self.forms[index]

def Template(*forms):
    """
    Decorator function that acts as the glue between a class and the Command Template system.
    """
    def decorator(target):
        formList = []
        for form in forms:
            formList.append(form)
        
        target.__template__ = TemplateBridge(formList)
            
        return target
    return decorator