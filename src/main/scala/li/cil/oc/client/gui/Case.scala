package li.cil.oc.client.gui

import java.util

import li.cil.oc.Localization
import li.cil.oc.client.Textures
import li.cil.oc.client.{PacketSender => ClientPacketSender}
import li.cil.oc.common.container
import li.cil.oc.common.tileentity
import net.minecraft.client.gui.GuiButton
import net.minecraft.entity.player.InventoryPlayer
import org.lwjgl.opengl.GL11

class Case(playerInventory: InventoryPlayer, val computer: tileentity.Case) extends DynamicGuiContainer(new container.Case(playerInventory, computer)) {
  protected var powerButton: ImageButton = _

  def add[T](list: util.List[T], value: Any) = list.add(value.asInstanceOf[T])

  protected override def actionPerformed(button: GuiButton) {
    if (button.id == 0) {
      ClientPacketSender.sendComputerPower(computer, !computer.isRunning)
    }
  }

  override def drawScreen(mouseX: Int, mouseY: Int, dt: Float) {
    powerButton.toggled = computer.isRunning
    super.drawScreen(mouseX, mouseY, dt)
  }

  override def initGui() {
    super.initGui()
    powerButton = new ImageButton(0, guiLeft + 70, guiTop + 33, 18, 18, Textures.GUI.ButtonPower, canToggle = true)
    add(buttonList, powerButton)
  }

  override protected def drawSecondaryForegroundLayer(mouseX: Int, mouseY: Int) = {
    super.drawSecondaryForegroundLayer(mouseX, mouseY)
    fontRendererObj.drawString(
      Localization.localizeImmediately(computer.getName),
      8, 6, 0x404040)
    if (powerButton.isMouseOver) {
      val tooltip = new java.util.ArrayList[String]
      tooltip.add(if (computer.isRunning) Localization.Computer.TurnOff else Localization.Computer.TurnOn)
      copiedDrawHoveringText(tooltip, mouseX - guiLeft, mouseY - guiTop, fontRendererObj)
    }
  }

  override def drawSecondaryBackgroundLayer() {
    GL11.glColor3f(1, 1, 1) // Required under Linux.
    mc.renderEngine.bindTexture(Textures.GUI.Computer)
    drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize)
  }

  override def doesGuiPauseGame = false
}