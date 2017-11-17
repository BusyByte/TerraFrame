package org.terraframe

import enumeratum.values._

sealed abstract class Block(val value: Int) extends IntEnumEntry {
  @inline def id = value
}

case object Block extends IntEnum[Block] {

  case object AirBlock                      extends Block(0)
  case object DirtBlock                     extends Block(1)
  case object StoneBlock                    extends Block(2)
  case object CopperOreBlock                extends Block(3)
  case object IronOreBlock                  extends Block(4)
  case object SilverOreBlock                extends Block(5)
  case object GoldOreBlock                  extends Block(6)
  case object WoodBlock                     extends Block(7)
  case object WorkbenchBlock                extends Block(8)
  case object WoodenChestBlock              extends Block(9)
  case object StoneChestBlock               extends Block(10)
  case object CopperChestBlock              extends Block(11)
  case object IronChestBlock                extends Block(12)
  case object SilverChestBlock              extends Block(13)
  case object GoldChestBlock                extends Block(14)
  case object TreeBlock                     extends Block(15)
  case object LeavesBlock                   extends Block(16)
  case object FurnaceBlock                  extends Block(17)
  case object CoalBlock                     extends Block(18)
  case object LumenstoneBlock               extends Block(19)
  case object WoodenTorchBlock              extends Block(20)
  case object CoalTorchBlock                extends Block(21)
  case object LumenstoneTorchBlock          extends Block(22)
  case object FurnaceOnBlock                extends Block(23)
  case object WoodenTorchLeftWallBlock      extends Block(24)
  case object WoodenTorchRightWallBlock     extends Block(25)
  case object CoalTorchLeftWallBlock        extends Block(26)
  case object CoalTorchRightWallBlock       extends Block(27)
  case object LumenstoneTorchLeftWallBlock  extends Block(28)
  case object LumenstoneTorchRightWallBlock extends Block(29)
  case object TreeRootBlock                 extends Block(30)
  case object ZincOreBlock                  extends Block(31)
  case object RhymestoneOreBlock            extends Block(32)
  case object ObduriteOreBlock              extends Block(33)
  case object AluminumOreBlock              extends Block(34)
  case object LeadOreBlock                  extends Block(35)
  case object UraniumOreBlock               extends Block(36)
  case object ZythiumOreBlock               extends Block(37)
  case object ZythiumOreOnBlock             extends Block(38)
  case object SiliconOreBlock               extends Block(39)
  case object IrradiumOreBlock              extends Block(40)
  case object NullstoneBlock                extends Block(41)
  case object MeltstoneBlock                extends Block(42)
  case object SkystoneBlock                 extends Block(43)
  case object MagnetiteOreBlock             extends Block(44)
  case object SandBlock                     extends Block(45)
  case object SnowBlock                     extends Block(46)
  case object GlassBlock                    extends Block(47)
  case object SunflowerStage1Block          extends Block(48)
  case object SunflowerStage2Block          extends Block(49)
  case object SunflowerStage3Block          extends Block(50)
  case object MoonflowerStage1Block         extends Block(51)
  case object MoonflowerStage2Block         extends Block(52)
  case object MoonflowerStage3Block         extends Block(53)
  case object DryweedStage1Block            extends Block(54)
  case object DryweedStage2Block            extends Block(55)
  case object DryweedStage3Block            extends Block(56)
  case object GreenleafStage1Block          extends Block(57)
  case object GreenleafStage2Block          extends Block(58)
  case object GreenleafStage3Block          extends Block(59)
  case object FrostleafStage1Block          extends Block(60)
  case object FrostleafStage2Block          extends Block(61)
  case object FrostleafStage3Block          extends Block(62)
  case object CaverootStage1Block           extends Block(63)
  case object CaverootStage2Block           extends Block(64)
  case object CaverootStage3Block           extends Block(65)
  case object SkyblossomStage1Block         extends Block(66)
  case object SkyblossomStage2Block         extends Block(67)
  case object SkyblossomStage3Block         extends Block(68)
  case object VoidRotStage1Block            extends Block(69)
  case object VoidRotStage2Block            extends Block(70)
  case object VoidRotStage3Block            extends Block(71)
  case object GrassBlock                    extends Block(72)
  case object JungleGrassBlock              extends Block(73)
  case object SwampGrassBlock               extends Block(74)
  case object MudBlock                      extends Block(75)
  case object SandstoneBlock                extends Block(76)
  case object MarshleafStage1Block          extends Block(77)
  case object MarshleafStage2Block          extends Block(78)
  case object MarshleafStage3Block          extends Block(79)
  case object ZincChestBlock                extends Block(80)
  case object RhymestoneChestBlock          extends Block(81)
  case object ObduriteChestBlock            extends Block(82)
  case object TreeNoBarkBlock               extends Block(83)
  case object CobblestoneBlock              extends Block(84)
  case object ChiseledStoneBlock            extends Block(85)
  case object ChiseledCobblestoneBlock      extends Block(86)
  case object StoneBricksBlock              extends Block(87)
  case object ClayBlock                     extends Block(88)
  case object ClayBricksBlock               extends Block(89)
  case object VarnishedWoodBlock            extends Block(90)
  case object DirtTransparentBlock          extends Block(91)
  case object MagnetiteOreTransparentBlock  extends Block(92)
  case object GrasstransparentBlock         extends Block(93)
  case object ZythiumWireBlock              extends Block(94)
  case object ZythiumWire1PowerBlock        extends Block(95)
  case object ZythiumWire2PowerBlock        extends Block(96)
  case object ZythiumWire3PowerBlock        extends Block(97)
  case object ZythiumWire4PowerBlock        extends Block(98)
  case object ZythiumWire5PowerBlock        extends Block(99)
  case object ZythiumTorchBlock             extends Block(100)
  case object ZythiumTorchLeftWallBlock     extends Block(101)
  case object ZythiumTorchRightWallBlock    extends Block(102)
  case object ZythiumLampBlock              extends Block(103)
  case object ZythiumLampOnBlock            extends Block(104)
  case object LeverBlock                    extends Block(105)
  case object LeverOnBlock                  extends Block(106)
  case object LeverLeftWallBlock            extends Block(107)
  case object LeverLeftWallOnBlock          extends Block(108)
  case object LeverLightWallBlock           extends Block(109)
  case object LeverRightWallOnBlock         extends Block(110)
  case object ZythiumAmplifierRightBlock    extends Block(111)
  case object ZythiumAmplifierDownBlock     extends Block(112)
  case object ZythiumAmplifierLeftBlock     extends Block(113)
  case object ZythiumAmplifierUpBlock       extends Block(114)
  case object ZythiumAmplifierRightOnBlock  extends Block(115)
  case object ZythiumAmplifierDownOnBlock   extends Block(116)
  case object ZythiumAmplifierLeftOnBlock   extends Block(117)
  case object ZythiumAmplifierUpOnBlock     extends Block(118)
  case object ZythiumInverterRightBlock     extends Block(119)
  case object ZythiumInverterDownBlock      extends Block(120)
  case object ZythiumInverterLeftBlock      extends Block(121)
  case object ZythiumInverterUpBlock        extends Block(122)
  case object ZythiumInverterRightOnBlock   extends Block(123)
  case object ZythiumInverterDownOnBlock    extends Block(124)
  case object ZythiumInverterLeftOnBlock    extends Block(125)
  case object ZythiumInverterUpOnBlock      extends Block(126)
  case object ButtonLeftBlock               extends Block(127)
  case object ButtonLeftOnBlock             extends Block(128)
  case object ButtonRightBlock              extends Block(129)
  case object ButtonRightOnBlock            extends Block(130)
  case object WoodenPressurePlateBlock      extends Block(131)
  case object WoodenPressurePlateOnBlock    extends Block(132)
  case object StonePressurePlateBlock       extends Block(133)
  case object StonePressurePlateOnBlock     extends Block(134)
  case object ZythiumPressurePlateBlock     extends Block(135)
  case object ZythiumPressurePlateOnBlock   extends Block(136)
  case object ZythiumDelayer1RightBlock     extends Block(137)
  case object ZythiumDelayer1DownBlock      extends Block(138)
  case object ZythiumDelayer1LeftBlock      extends Block(139)
  case object ZythiumDelayer1UpBlock        extends Block(140)
  case object ZythiumDelayer1RightOnBlock   extends Block(141)
  case object ZythiumDelayer1DownOnBlock    extends Block(142)
  case object ZythiumDelayer1LeftOnBlock    extends Block(143)
  case object ZythiumDelayer1UpOnBlock      extends Block(144)
  case object ZythiumDelayer2RightBlock     extends Block(145)
  case object ZythiumDelayer2DownBlock      extends Block(146)
  case object ZythiumDelayer2LeftBlock      extends Block(147)
  case object ZythiumDelayer2UpBlock        extends Block(148)
  case object ZythiumDelayer2RightOnBlock   extends Block(149)
  case object ZythiumDelayer2DownOnBlock    extends Block(150)
  case object ZythiumDelayer2LeftOnBlock    extends Block(151)
  case object ZythiumDelayer2UpOnBlock      extends Block(152)
  case object ZythiumDelayer4RightBlock     extends Block(153)
  case object ZythiumDelayer4DownBlock      extends Block(154)
  case object ZythiumDelayer4LeftBlock      extends Block(155)
  case object ZythiumDelayer4UpBlock        extends Block(156)
  case object ZythiumDelayer4RightOnBlock   extends Block(157)
  case object ZythiumDelayer4DownOnBlock    extends Block(158)
  case object ZythiumDelayer4LeftOnBlock    extends Block(159)
  case object ZythiumDelayer4UpOnBlock      extends Block(160)
  case object ZythiumDelayer8RightBlock     extends Block(161)
  case object ZythiumDelayer8DownBlock      extends Block(162)
  case object ZythiumDelayer8LeftBlock      extends Block(163)
  case object ZythiumDelayer8UpBlock        extends Block(164)
  case object ZythiumDelayer8RightOnBlock   extends Block(165)
  case object ZythiumDelayer8DownOnBlock    extends Block(166)
  case object ZythiumDelayer8LeftOnBlock    extends Block(167)
  case object ZythiumDelayer8UpOnBlock      extends Block(168)

  val values = findValues

  def withId(id: Int) = withValue(id)

  def drop(block: Block): Option[UiItem] = block match {
    case DirtBlock                     => Some(DirtUiItem)
    case StoneBlock                    => Some(StoneUiItem)
    case CopperOreBlock                => Some(CopperOreUiItem)
    case IronOreBlock                  => Some(IronOreUiItem)
    case SilverOreBlock                => Some(SilverOreUiItem)
    case GoldOreBlock                  => Some(GoldOreUiItem)
    case WoodBlock                     => Some(WoodUiItem)
    case WorkbenchBlock                => Some(WorkbenchUiItem)
    case WoodenChestBlock              => Some(WoodenChestUiItem)
    case StoneChestBlock               => Some(StoneChestUiItem)
    case CopperChestBlock              => Some(CopperChestUiItem)
    case IronChestBlock                => Some(IronChestUiItem)
    case SilverChestBlock              => Some(SilverChestUiItem)
    case GoldChestBlock                => Some(GoldChestUiItem)
    case TreeBlock                     => Some(WoodUiItem)
    case FurnaceBlock                  => Some(FurnaceUiItem)
    case CoalBlock                     => Some(CoalUiItem)
    case LumenstoneBlock               => Some(LumenstoneUiItem)
    case WoodenTorchBlock              => Some(WoodenTorchUiItem)
    case CoalTorchBlock                => Some(CoalTorchUiItem)
    case LumenstoneTorchBlock          => Some(LumenstoneTorchUiItem)
    case FurnaceOnBlock                => Some(FurnaceUiItem)
    case WoodenTorchLeftWallBlock      => Some(WoodenTorchUiItem)
    case WoodenTorchRightWallBlock     => Some(WoodenTorchUiItem)
    case CoalTorchLeftWallBlock        => Some(CoalTorchUiItem)
    case CoalTorchRightWallBlock       => Some(CoalTorchUiItem)
    case LumenstoneTorchLeftWallBlock  => Some(LumenstoneTorchUiItem)
    case LumenstoneTorchRightWallBlock => Some(LumenstoneTorchUiItem)
    case ZincOreBlock                  => Some(ZincOreUiItem)
    case RhymestoneOreBlock            => Some(RhymestoneOreUiItem)
    case ObduriteOreBlock              => Some(ObduriteOreUiItem)
    case AluminumOreBlock              => Some(AluminumOreUiItem)
    case LeadOreBlock                  => Some(LeadOreUiItem)
    case UraniumOreBlock               => Some(UraniumOreUiItem)
    case ZythiumOreBlock               => Some(ZythiumOreUiItem)
    case ZythiumOreOnBlock             => Some(ZythiumOreUiItem)
    case SiliconOreBlock               => Some(SiliconOreUiItem)
    case IrradiumOreBlock              => Some(IrradiumOreUiItem)
    case NullstoneBlock                => Some(NullstoneUiItem)
    case MeltstoneBlock                => Some(MeltstoneUiItem)
    case SkystoneBlock                 => Some(SkystoneUiItem)
    case MagnetiteOreBlock             => Some(MagnetiteOreUiItem)
    case SandBlock                     => Some(SandUiItem)
    case SnowBlock                     => Some(SnowUiItem)
    case SunflowerStage3Block          => Some(SunflowerUiItem)
    case MoonflowerStage3Block         => Some(MoonflowerUiItem)
    case DryweedStage3Block            => Some(DryweedUiItem)
    case GreenleafStage3Block          => Some(GreenleafUiItem)
    case FrostleafStage3Block          => Some(FrostleafUiItem)
    case CaverootStage3Block           => Some(CaverootUiItem)
    case SkyblossomStage3Block         => Some(SkyblossomUiItem)
    case VoidRotStage3Block            => Some(VoidRotUiItem)
    case GrassBlock                    => Some(DirtUiItem)
    case JungleGrassBlock              => Some(DirtUiItem)
    case SwampGrassBlock               => Some(MudUiItem)
    case MudBlock                      => Some(MudUiItem)
    case SandstoneBlock                => Some(SandstoneUiItem)
    case MarshleafStage3Block          => Some(MarshleafUiItem)
    case ZincChestBlock                => Some(ZincChestUiItem)
    case RhymestoneChestBlock          => Some(RhymestoneChestUiItem)
    case ObduriteChestBlock            => Some(ObduriteChestUiItem)
    case TreeNoBarkBlock               => Some(WoodUiItem)
    case CobblestoneBlock              => Some(CobblestoneUiItem)
    case ChiseledStoneBlock            => Some(ChiseledStoneUiItem)
    case ChiseledCobblestoneBlock      => Some(ChiseledCobblestoneUiItem)
    case StoneBricksBlock              => Some(StoneBricksUiItem)
    case ClayBlock                     => Some(ClayUiItem)
    case ClayBricksBlock               => Some(ClayBricksUiItem)
    case VarnishedWoodBlock            => Some(VarnishedWoodUiItem)
    case DirtTransparentBlock          => Some(DirtUiItem)
    case MagnetiteOreTransparentBlock  => Some(MagnetiteOreUiItem)
    case GrasstransparentBlock         => Some(DirtUiItem)
    case ZythiumWireBlock              => Some(ZythiumWireUiItem)
    case ZythiumWire1PowerBlock        => Some(ZythiumWireUiItem)
    case ZythiumWire2PowerBlock        => Some(ZythiumWireUiItem)
    case ZythiumWire3PowerBlock        => Some(ZythiumWireUiItem)
    case ZythiumWire4PowerBlock        => Some(ZythiumWireUiItem)
    case ZythiumWire5PowerBlock        => Some(ZythiumWireUiItem)
    case ZythiumTorchBlock             => Some(ZythiumTorchUiItem)
    case ZythiumTorchLeftWallBlock     => Some(ZythiumTorchUiItem)
    case ZythiumTorchRightWallBlock    => Some(ZythiumTorchUiItem)
    case ZythiumLampBlock              => Some(ZythiumTorchUiItem)
    case ZythiumLampOnBlock            => Some(ZythiumTorchUiItem)
    case LeverBlock                    => Some(ZythiumLampUiItem)
    case LeverOnBlock                  => Some(ZythiumLampUiItem)
    case LeverLeftWallBlock            => Some(ZythiumLampUiItem)
    case LeverLeftWallOnBlock          => Some(ZythiumLampUiItem)
    case LeverLightWallBlock           => Some(ZythiumLampUiItem)
    case LeverRightWallOnBlock         => Some(ZythiumLampUiItem)
    case ZythiumAmplifierRightBlock    => Some(ZythiumAmplifierUiItem)
    case ZythiumAmplifierDownBlock     => Some(ZythiumAmplifierUiItem)
    case ZythiumAmplifierLeftBlock     => Some(ZythiumAmplifierUiItem)
    case ZythiumAmplifierUpBlock       => Some(ZythiumAmplifierUiItem)
    case ZythiumAmplifierRightOnBlock  => Some(ZythiumAmplifierUiItem)
    case ZythiumAmplifierDownOnBlock   => Some(ZythiumAmplifierUiItem)
    case ZythiumAmplifierLeftOnBlock   => Some(ZythiumAmplifierUiItem)
    case ZythiumAmplifierUpOnBlock     => Some(ZythiumAmplifierUiItem)
    case ZythiumInverterRightBlock     => Some(ZythiumInverterUiItem)
    case ZythiumInverterDownBlock      => Some(ZythiumInverterUiItem)
    case ZythiumInverterLeftBlock      => Some(ZythiumInverterUiItem)
    case ZythiumInverterUpBlock        => Some(ZythiumInverterUiItem)
    case ZythiumInverterRightOnBlock   => Some(ZythiumInverterUiItem)
    case ZythiumInverterDownOnBlock    => Some(ZythiumInverterUiItem)
    case ZythiumInverterLeftOnBlock    => Some(ZythiumInverterUiItem)
    case ZythiumInverterUpOnBlock      => Some(ZythiumInverterUiItem)
    case ButtonLeftBlock               => Some(ButtonUiItem)
    case ButtonLeftOnBlock             => Some(ButtonUiItem)
    case ButtonRightBlock              => Some(ButtonUiItem)
    case ButtonRightOnBlock            => Some(ButtonUiItem)
    case WoodenPressurePlateBlock      => Some(WoodenPressurePlateUiItem)
    case WoodenPressurePlateOnBlock    => Some(WoodenPressurePlateUiItem)
    case StonePressurePlateBlock       => Some(StonePressurePlateUiItem)
    case StonePressurePlateOnBlock     => Some(StonePressurePlateUiItem)
    case ZythiumPressurePlateBlock     => Some(ZythiumPressurePlateUiItem)
    case ZythiumPressurePlateOnBlock   => Some(ZythiumPressurePlateUiItem)
    case ZythiumDelayer1RightBlock     => Some(ZythiumDelayer1UiItem)
    case ZythiumDelayer1DownBlock      => Some(ZythiumDelayer1UiItem)
    case ZythiumDelayer1LeftBlock      => Some(ZythiumDelayer1UiItem)
    case ZythiumDelayer1UpBlock        => Some(ZythiumDelayer1UiItem)
    case ZythiumDelayer1RightOnBlock   => Some(ZythiumDelayer1UiItem)
    case ZythiumDelayer1DownOnBlock    => Some(ZythiumDelayer1UiItem)
    case ZythiumDelayer1LeftOnBlock    => Some(ZythiumDelayer1UiItem)
    case ZythiumDelayer1UpOnBlock      => Some(ZythiumDelayer1UiItem)
    case ZythiumDelayer2RightBlock     => Some(ZythiumDelayer2UiItem)
    case ZythiumDelayer2DownBlock      => Some(ZythiumDelayer2UiItem)
    case ZythiumDelayer2LeftBlock      => Some(ZythiumDelayer2UiItem)
    case ZythiumDelayer2UpBlock        => Some(ZythiumDelayer2UiItem)
    case ZythiumDelayer2RightOnBlock   => Some(ZythiumDelayer2UiItem)
    case ZythiumDelayer2DownOnBlock    => Some(ZythiumDelayer2UiItem)
    case ZythiumDelayer2LeftOnBlock    => Some(ZythiumDelayer2UiItem)
    case ZythiumDelayer2UpOnBlock      => Some(ZythiumDelayer2UiItem)
    case ZythiumDelayer4RightBlock     => Some(ZythiumDelayer4UiItem)
    case ZythiumDelayer4DownBlock      => Some(ZythiumDelayer4UiItem)
    case ZythiumDelayer4LeftBlock      => Some(ZythiumDelayer4UiItem)
    case ZythiumDelayer4UpBlock        => Some(ZythiumDelayer4UiItem)
    case ZythiumDelayer4RightOnBlock   => Some(ZythiumDelayer4UiItem)
    case ZythiumDelayer4DownOnBlock    => Some(ZythiumDelayer4UiItem)
    case ZythiumDelayer4LeftOnBlock    => Some(ZythiumDelayer4UiItem)
    case ZythiumDelayer4UpOnBlock      => Some(ZythiumDelayer4UiItem)
    case ZythiumDelayer8RightBlock     => Some(ZythiumDelayer8UiItem)
    case ZythiumDelayer8DownBlock      => Some(ZythiumDelayer8UiItem)
    case ZythiumDelayer8LeftBlock      => Some(ZythiumDelayer8UiItem)
    case ZythiumDelayer8UpBlock        => Some(ZythiumDelayer8UiItem)
    case ZythiumDelayer8RightOnBlock   => Some(ZythiumDelayer8UiItem)
    case ZythiumDelayer8DownOnBlock    => Some(ZythiumDelayer8UiItem)
    case ZythiumDelayer8LeftOnBlock    => Some(ZythiumDelayer8UiItem)
    case ZythiumDelayer8UpOnBlock      => Some(ZythiumDelayer8UiItem)
    case _                             => None
  }

  lazy val blockTools: Map[Block, Set[UiItem]] = {

    val l1ToolSet = Set[UiItem](
      CopperPickUiItem,
      IronPickUiItem,
      SilverPickUiItem,
      GoldPickUiItem,
      ZincPickUiItem,
      RhymestonePickUiItem,
      ObduritePickUiItem,
      AluminumPickUiItem,
      LeadPickUiItem,
      WoodenPickUiItem,
      StonePickUiItem,
      MagnetitePickUiItem,
      IrradiumPickUiItem
    )
    val l2ToolSet = Set[UiItem](
      CopperAxeUiItem,
      IronAxeUiItem,
      SilverAxeUiItem,
      GoldAxeUiItem,
      ZincAxeUiItem,
      RhymestoneAxeUiItem,
      ObduriteAxeUiItem,
      AluminumAxeUiItem,
      LeadAxeUiItem,
      WoodenAxeUiItem,
      StoneAxeUiItem,
      MagnetiteAxeUiItem,
      IrradiumAxeUiItem
    )
    val l3ToolSet = Set[UiItem](
      CopperPickUiItem,
      IronPickUiItem,
      SilverPickUiItem,
      GoldPickUiItem,
      CopperAxeUiItem,
      IronAxeUiItem,
      SilverAxeUiItem,
      GoldAxeUiItem,
      ZincPickUiItem,
      ZincAxeUiItem,
      RhymestonePickUiItem,
      RhymestoneAxeUiItem,
      ObduritePickUiItem,
      ObduriteAxeUiItem,
      AluminumPickUiItem,
      AluminumAxeUiItem,
      LeadPickUiItem,
      LeadAxeUiItem,
      WoodenPickUiItem,
      WoodenAxeUiItem,
      StonePickUiItem,
      StoneAxeUiItem,
      MagnetitePickUiItem,
      MagnetiteAxeUiItem,
      IrradiumPickUiItem,
      IrradiumAxeUiItem
    )
    val l4ToolSet = Set[UiItem](
      CopperPickUiItem,
      IronPickUiItem,
      SilverPickUiItem,
      GoldPickUiItem,
      ZincPickUiItem,
      RhymestonePickUiItem,
      ObduritePickUiItem,
      AluminumPickUiItem,
      LeadPickUiItem,
      WoodenPickUiItem,
      StonePickUiItem
    )
    val l5ToolSet = Set[UiItem](
      CopperPickUiItem,
      IronPickUiItem,
      SilverPickUiItem,
      GoldPickUiItem,
      CopperAxeUiItem,
      IronAxeUiItem,
      SilverAxeUiItem,
      GoldAxeUiItem,
      ZincPickUiItem,
      ZincAxeUiItem,
      RhymestonePickUiItem,
      RhymestoneAxeUiItem,
      ObduritePickUiItem,
      ObduriteAxeUiItem
    )

    List[(Block, Set[UiItem])](
      AirBlock                      -> Set[UiItem](),
      DirtBlock                     -> l1ToolSet,
      StoneBlock                    -> l1ToolSet,
      CopperOreBlock                -> l1ToolSet,
      IronOreBlock                  -> l1ToolSet,
      SilverOreBlock                -> l1ToolSet,
      GoldOreBlock                  -> l1ToolSet,
      WoodBlock                     -> l2ToolSet,
      WorkbenchBlock                -> l2ToolSet,
      WoodenChestBlock              -> l2ToolSet,
      StoneChestBlock               -> l1ToolSet,
      CopperChestBlock              -> l1ToolSet,
      IronChestBlock                -> l1ToolSet,
      SilverChestBlock              -> l1ToolSet,
      GoldChestBlock                -> l1ToolSet,
      TreeBlock                     -> l2ToolSet,
      FurnaceBlock                  -> l1ToolSet,
      CoalBlock                     -> l1ToolSet,
      LumenstoneBlock               -> l1ToolSet,
      WoodenTorchBlock              -> l3ToolSet,
      CoalTorchBlock                -> l3ToolSet,
      LumenstoneTorchBlock          -> l3ToolSet,
      FurnaceOnBlock                -> l1ToolSet,
      WoodenTorchLeftWallBlock      -> l3ToolSet,
      WoodenTorchRightWallBlock     -> l3ToolSet,
      CoalTorchLeftWallBlock        -> l3ToolSet,
      CoalTorchRightWallBlock       -> l3ToolSet,
      LumenstoneTorchLeftWallBlock  -> l3ToolSet,
      LumenstoneTorchRightWallBlock -> l1ToolSet,
      ZincOreBlock                  -> l1ToolSet,
      RhymestoneOreBlock            -> l1ToolSet,
      ObduriteOreBlock              -> l1ToolSet,
      AluminumOreBlock              -> l1ToolSet,
      LeadOreBlock                  -> l1ToolSet,
      UraniumOreBlock               -> l1ToolSet,
      ZythiumOreBlock               -> l1ToolSet,
      ZythiumOreOnBlock             -> l1ToolSet,
      SiliconOreBlock               -> l1ToolSet,
      IrradiumOreBlock              -> l1ToolSet,
      NullstoneBlock                -> l1ToolSet,
      MeltstoneBlock                -> l1ToolSet,
      SkystoneBlock                 -> l1ToolSet,
      MagnetiteOreBlock             -> l1ToolSet,
      SandBlock                     -> l1ToolSet,
      SnowBlock                     -> l1ToolSet,
      GlassBlock                    -> l3ToolSet,
      SunflowerStage1Block          -> l3ToolSet,
      SunflowerStage2Block          -> l3ToolSet,
      SunflowerStage3Block          -> l3ToolSet,
      MoonflowerStage1Block         -> l3ToolSet,
      MoonflowerStage2Block         -> l3ToolSet,
      MoonflowerStage3Block         -> l3ToolSet,
      DryweedStage1Block            -> l3ToolSet,
      DryweedStage2Block            -> l3ToolSet,
      DryweedStage3Block            -> l3ToolSet,
      GreenleafStage1Block          -> l3ToolSet,
      GreenleafStage2Block          -> l3ToolSet,
      GreenleafStage3Block          -> l3ToolSet,
      FrostleafStage1Block          -> l3ToolSet,
      FrostleafStage2Block          -> l3ToolSet,
      FrostleafStage3Block          -> l3ToolSet,
      CaverootStage1Block           -> l3ToolSet,
      CaverootStage2Block           -> l3ToolSet,
      CaverootStage3Block           -> l3ToolSet,
      SkyblossomStage1Block         -> l3ToolSet,
      SkyblossomStage2Block         -> l3ToolSet,
      SkyblossomStage3Block         -> l3ToolSet,
      VoidRotStage1Block            -> l3ToolSet,
      VoidRotStage2Block            -> l3ToolSet,
      VoidRotStage3Block            -> l1ToolSet,
      GrassBlock                    -> l1ToolSet,
      JungleGrassBlock              -> l1ToolSet,
      SwampGrassBlock               -> l1ToolSet,
      MudBlock                      -> l1ToolSet,
      SandstoneBlock                -> l1ToolSet,
      MarshleafStage1Block          -> l3ToolSet,
      MarshleafStage2Block          -> l3ToolSet,
      MarshleafStage3Block          -> l5ToolSet,
      ZincChestBlock                -> l1ToolSet,
      RhymestoneChestBlock          -> l1ToolSet,
      ObduriteChestBlock            -> l1ToolSet,
      TreeNoBarkBlock               -> l2ToolSet,
      CobblestoneBlock              -> l1ToolSet,
      ChiseledStoneBlock            -> l1ToolSet,
      ChiseledCobblestoneBlock      -> l1ToolSet,
      StoneBricksBlock              -> l1ToolSet,
      ClayBlock                     -> l1ToolSet,
      ClayBricksBlock               -> l1ToolSet,
      VarnishedWoodBlock            -> l2ToolSet,
      DirtTransparentBlock          -> l1ToolSet,
      MagnetiteOreTransparentBlock  -> l1ToolSet,
      GrasstransparentBlock         -> l4ToolSet,
      ZythiumWireBlock              -> l3ToolSet,
      ZythiumWire1PowerBlock        -> l3ToolSet,
      ZythiumWire2PowerBlock        -> l3ToolSet,
      ZythiumWire3PowerBlock        -> l3ToolSet,
      ZythiumWire4PowerBlock        -> l3ToolSet,
      ZythiumWire5PowerBlock        -> l3ToolSet,
      ZythiumTorchBlock             -> l3ToolSet,
      ZythiumTorchLeftWallBlock     -> l3ToolSet,
      ZythiumTorchRightWallBlock    -> l3ToolSet,
      ZythiumLampBlock              -> l1ToolSet,
      ZythiumLampOnBlock            -> l1ToolSet,
      LeverBlock                    -> l1ToolSet,
      LeverOnBlock                  -> l1ToolSet,
      LeverLeftWallBlock            -> l1ToolSet,
      LeverLeftWallOnBlock          -> l1ToolSet,
      LeverLightWallBlock           -> l1ToolSet,
      LeverRightWallOnBlock         -> l1ToolSet,
      ZythiumAmplifierRightBlock    -> l1ToolSet,
      ZythiumAmplifierDownBlock     -> l1ToolSet,
      ZythiumAmplifierLeftBlock     -> l1ToolSet,
      ZythiumAmplifierUpBlock       -> l1ToolSet,
      ZythiumAmplifierRightOnBlock  -> l1ToolSet,
      ZythiumAmplifierDownOnBlock   -> l1ToolSet,
      ZythiumAmplifierLeftOnBlock   -> l1ToolSet,
      ZythiumAmplifierUpOnBlock     -> l1ToolSet,
      ZythiumInverterRightBlock     -> l1ToolSet,
      ZythiumInverterDownBlock      -> l1ToolSet,
      ZythiumInverterLeftBlock      -> l1ToolSet,
      ZythiumInverterUpBlock        -> l1ToolSet,
      ZythiumInverterRightOnBlock   -> l1ToolSet,
      ZythiumInverterDownOnBlock    -> l1ToolSet,
      ZythiumInverterLeftOnBlock    -> l1ToolSet,
      ZythiumInverterUpOnBlock      -> l1ToolSet,
      ButtonLeftBlock               -> l1ToolSet,
      ButtonLeftOnBlock             -> l1ToolSet,
      ButtonRightBlock              -> l1ToolSet,
      ButtonRightOnBlock            -> l1ToolSet,
      WoodenPressurePlateBlock      -> l2ToolSet,
      WoodenPressurePlateOnBlock    -> l2ToolSet,
      StonePressurePlateBlock       -> l1ToolSet,
      StonePressurePlateOnBlock     -> l1ToolSet,
      ZythiumPressurePlateBlock     -> l1ToolSet,
      ZythiumPressurePlateOnBlock   -> l1ToolSet,
      ZythiumDelayer1RightBlock     -> l1ToolSet,
      ZythiumDelayer1DownBlock      -> l1ToolSet,
      ZythiumDelayer1LeftBlock      -> l1ToolSet,
      ZythiumDelayer1UpBlock        -> l1ToolSet,
      ZythiumDelayer1RightOnBlock   -> l1ToolSet,
      ZythiumDelayer1DownOnBlock    -> l1ToolSet,
      ZythiumDelayer1LeftOnBlock    -> l1ToolSet,
      ZythiumDelayer1UpOnBlock      -> l1ToolSet,
      ZythiumDelayer2RightBlock     -> l1ToolSet,
      ZythiumDelayer2DownBlock      -> l1ToolSet,
      ZythiumDelayer2LeftBlock      -> l1ToolSet,
      ZythiumDelayer2UpBlock        -> l1ToolSet,
      ZythiumDelayer2RightOnBlock   -> l1ToolSet,
      ZythiumDelayer2DownOnBlock    -> l1ToolSet,
      ZythiumDelayer2LeftOnBlock    -> l1ToolSet,
      ZythiumDelayer2UpOnBlock      -> l1ToolSet,
      ZythiumDelayer4RightBlock     -> l1ToolSet,
      ZythiumDelayer4DownBlock      -> l1ToolSet,
      ZythiumDelayer4LeftBlock      -> l1ToolSet,
      ZythiumDelayer4UpBlock        -> l1ToolSet,
      ZythiumDelayer4RightOnBlock   -> l1ToolSet,
      ZythiumDelayer4DownOnBlock    -> l1ToolSet,
      ZythiumDelayer4LeftOnBlock    -> l1ToolSet,
      ZythiumDelayer4UpOnBlock      -> l1ToolSet,
      ZythiumDelayer8RightBlock     -> l1ToolSet,
      ZythiumDelayer8DownBlock      -> l1ToolSet,
      ZythiumDelayer8LeftBlock      -> l1ToolSet,
      ZythiumDelayer8UpBlock        -> l1ToolSet,
      ZythiumDelayer8RightOnBlock   -> l1ToolSet,
      ZythiumDelayer8DownOnBlock    -> l1ToolSet,
      ZythiumDelayer8LeftOnBlock    -> l1ToolSet,
      ZythiumDelayer8UpOnBlock      -> l1ToolSet
    ).toMap
  }

  def blockContainsTool(block: Block, uiItem: UiItem): Boolean = {
    blockTools.get(block).exists(_.contains(uiItem))
  }

  def isGsupported(block: Block): Boolean = block match {
    case TreeBlock                   => true
    case TreeNoBarkBlock             => true
    case WoodenTorchBlock            => true
    case CoalTorchBlock              => true
    case LumenstoneTorchBlock        => true
    case MarshleafStage1Block        => true
    case MarshleafStage2Block        => true
    case ZythiumTorchBlock           => true
    case LeverBlock                  => true
    case LeverOnBlock                => true
    case WoodenPressurePlateBlock    => true
    case WoodenPressurePlateOnBlock  => true
    case StonePressurePlateBlock     => true
    case StonePressurePlateOnBlock   => true
    case ZythiumPressurePlateBlock   => true
    case ZythiumPressurePlateOnBlock => true
    case SunflowerStage1Block        => true
    case SunflowerStage2Block        => true
    case SunflowerStage3Block        => true
    case MoonflowerStage1Block       => true
    case MoonflowerStage2Block       => true
    case MoonflowerStage3Block       => true
    case DryweedStage1Block          => true
    case DryweedStage2Block          => true
    case DryweedStage3Block          => true
    case GreenleafStage1Block        => true
    case GreenleafStage2Block        => true
    case GreenleafStage3Block        => true
    case FrostleafStage1Block        => true
    case FrostleafStage2Block        => true
    case FrostleafStage3Block        => true
    case CaverootStage1Block         => true
    case CaverootStage2Block         => true
    case CaverootStage3Block         => true
    case SkyblossomStage1Block       => true
    case SkyblossomStage2Block       => true
    case SkyblossomStage3Block       => true
    case VoidRotStage1Block          => true
    case VoidRotStage2Block          => true
    case VoidRotStage3Block          => true
    case GrassBlock                  => true
    case _                           => false

  }

  def powers(block: Block): Boolean = block match {
    case ZythiumTorchBlock            => true
    case ZythiumTorchLeftWallBlock    => true
    case ZythiumTorchRightWallBlock   => true
    case LeverOnBlock                 => true
    case LeverLeftWallOnBlock         => true
    case LeverRightWallOnBlock        => true
    case ZythiumAmplifierRightOnBlock => true
    case ZythiumAmplifierDownOnBlock  => true
    case ZythiumAmplifierLeftOnBlock  => true
    case ZythiumAmplifierUpOnBlock    => true
    case ZythiumInverterRightBlock    => true
    case ZythiumInverterDownBlock     => true
    case ZythiumInverterLeftBlock     => true
    case ZythiumInverterUpBlock       => true
    case ButtonLeftOnBlock            => true
    case ButtonRightOnBlock           => true
    case WoodenPressurePlateOnBlock   => true
    case StonePressurePlateOnBlock    => true
    case ZythiumPressurePlateOnBlock  => true
    case ZythiumDelayer1RightOnBlock  => true
    case ZythiumDelayer1DownOnBlock   => true
    case ZythiumDelayer1LeftOnBlock   => true
    case ZythiumDelayer1UpOnBlock     => true
    case ZythiumDelayer2RightOnBlock  => true
    case ZythiumDelayer2DownOnBlock   => true
    case ZythiumDelayer2LeftOnBlock   => true
    case ZythiumDelayer2UpOnBlock     => true
    case ZythiumDelayer4RightOnBlock  => true
    case ZythiumDelayer4DownOnBlock   => true
    case ZythiumDelayer4LeftOnBlock   => true
    case ZythiumDelayer4UpOnBlock     => true
    case ZythiumDelayer8RightOnBlock  => true
    case ZythiumDelayer8DownOnBlock   => true
    case ZythiumDelayer8LeftOnBlock   => true
    case ZythiumDelayer8UpOnBlock     => true
    case _                            => false
  }

  def lightIntensity(block: Block): Int = block match {
    case LumenstoneBlock               => 21
    case WoodenTorchBlock              => 15
    case CoalTorchBlock                => 18
    case LumenstoneTorchBlock          => 21
    case FurnaceOnBlock                => 15
    case WoodenTorchLeftWallBlock      => 15
    case WoodenTorchRightWallBlock     => 15
    case CoalTorchLeftWallBlock        => 18
    case CoalTorchRightWallBlock       => 19
    case LumenstoneTorchLeftWallBlock  => 21
    case LumenstoneTorchRightWallBlock => 21
    case UraniumOreBlock               => 15
    case ZythiumOreBlock               => 15
    case ZythiumOreOnBlock             => 18
    case MoonflowerStage1Block         => 15
    case MoonflowerStage2Block         => 15
    case MoonflowerStage3Block         => 15
    case ZythiumWire1PowerBlock        => 6
    case ZythiumWire2PowerBlock        => 7
    case ZythiumWire3PowerBlock        => 8
    case ZythiumWire4PowerBlock        => 9
    case ZythiumWire5PowerBlock        => 10
    case ZythiumTorchBlock             => 12
    case ZythiumTorchLeftWallBlock     => 12
    case ZythiumTorchRightWallBlock    => 12
    case ZythiumLampOnBlock            => 21
    case ZythiumAmplifierDownBlock     => 12
    case ZythiumAmplifierUpBlock       => 12
    case ZythiumAmplifierDownOnBlock   => 12
    case ZythiumAmplifierUpOnBlock     => 12
    case ZythiumInverterRightOnBlock   => 12
    case ZythiumInverterDownOnBlock    => 12
    case ZythiumInverterLeftOnBlock    => 12
    case ZythiumInverterUpOnBlock      => 12
    case ZythiumDelayer1RightBlock     => 12
    case ZythiumDelayer1DownBlock      => 12
    case ZythiumDelayer1LeftBlock      => 12
    case ZythiumDelayer1UpBlock        => 12
    case ZythiumDelayer2RightBlock     => 12
    case ZythiumDelayer2DownBlock      => 12
    case ZythiumDelayer2LeftBlock      => 12
    case ZythiumDelayer2UpBlock        => 12
    case ZythiumDelayer4RightBlock     => 12
    case ZythiumDelayer4DownBlock      => 12
    case ZythiumDelayer4LeftBlock      => 12
    case ZythiumDelayer4UpBlock        => 12
    case ZythiumDelayer8RightBlock     => 12
    case ZythiumDelayer8DownBlock      => 12
    case ZythiumDelayer8LeftBlock      => 12
    case ZythiumDelayer8UpBlock        => 12

    case _ => 0
  }

  def isLightBlock(block: Block): Boolean = lightIntensity(block) > 0
}
