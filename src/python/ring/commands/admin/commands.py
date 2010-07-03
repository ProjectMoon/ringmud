from ring.commands.annotations.pybridge import *
from ring.commands import Command
from ring.commands import CommandResult
from ring.mobiles.senses.stimuli import AudioStimulus
from ring.server import MUDConnectionManager as MCM

@Template(Form(clause = "#text"))
class Godvoice(Command):
    def __init__(self):
        self.commandName = "godvoice"
        
    def execute(self, sender, args):
        res = CommandResult()
        message = args.getArgument(0)
        textBackToPlayer = "You project your voice across the cosmos, saying, \"" + message + "\""
        textToOtherPlayers = "The voice of the gods rumbles in the sky: \"" + message + "\""
               
        #Being deaf doesn't stop the power of the gods.
        stim = AudioStimulus()
        stim.depiction, stim.deafDepiction = textToOtherPlayers
        
        for player in MCM.currentCharacters:
            if player is not sender:
                player.dynamicModel.sensesGroup.consume(stim)
        
        res.successful = True
        res.text = textBackToPlayer
        res.send()
    
    def rollback(self):
        pass
    

        